package lock.chapter12;

import atomic.ThreadUtil;
import sun.misc.Unsafe;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 公平锁实现,通过synchronized-wait-notify机制 和队列
 *
 * @author: mahao
 * @date: 2019/9/28
 */
public class FairLock implements Lock {

    private volatile int state;
    private Thread lockThread;
    private LinkedList<Node> waitQueue = new LinkedList<>();
    private final static Unsafe unsafe = ThreadUtil.getUnsafe();
    private final static long offset;

    static {
        try {
            offset = unsafe.objectFieldOffset(FairLock.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            throw new Error("init fail");
        }
    }


    public void lock() {
        for (; ; ) {
            if (state == 0) {
                if (!hasQueuedPredecessors() && compareAndSet(0, 1)) {
                    lockThread = Thread.currentThread();
                    return;
                }

            } else if (lockThread == Thread.currentThread()) {
                state++;
                return;
            }
            synchronized (waitQueue){
                addWait(Thread.currentThread());
            }

        }

    }

    private void addWait(Thread currentThread) {
        Node node = new Node(currentThread);
        waitQueue.addLast(node);
        try {
            node.doWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //判断是否需要排队
    private boolean hasQueuedPredecessors() {
        if (waitQueue.size() == 0) {
            return false;
        } else if (waitQueue.getFirst().currentThread == Thread.currentThread()) {
            return false;
        }
        return true;
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {
        if (Thread.currentThread() != lockThread)
            throw new IllegalMonitorStateException();
        int s = state - 1;
        boolean free = false;
        state = s;
        if (s == 0) {
            lockThread = null;
            if (waitQueue.size() != 0) {
                Node node = waitQueue.removeFirst();
                node.doNotify();
            }

        }

    }

    public Condition newCondition() {
        return null;
    }


    private boolean compareAndSet(int except, int update) {
        return unsafe.compareAndSwapInt(this, offset, except, update);
    }


    /**
     * 将每个等待线程封装成节点，自己唤醒自己
     */
    private static class Node {

        /**
         * 0 正常
         * -1 取消
         * 1 等待执行
         */
        private int state;
        private Thread currentThread;

        public Node(Thread currentThread) {
            this.currentThread = currentThread;
        }

        public synchronized void doWait() throws InterruptedException {
            state = 1;
            System.out.println("do wait " + ThreadUtil.getName());
            this.wait();
        }

        public synchronized void doNotify() {
            state = 2;
            this.notify();
        }
    }
}

class TestFairLock {


    public static void main(String[] args) {
        final Lock lock = new FairLock();
        for (int i = 0; i < 3 ; i++) {
            new Thread(i + "") {
                @Override
                public void run() {
                    while (true) {
                        lock.lock();
                        ThreadUtil.sleep(1000);
                        System.out.println(ThreadUtil.getName());
                        lock.unlock();
                    }
                }
            }.start();
            ThreadUtil.sleep(1000);
        }
    }

}
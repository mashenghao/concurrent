package chapter6;


import chapter3.ThreadUtil;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

import java.util.LinkedList;

/**
 * 手动实现可重入锁(synchronized实现)：
 * 1.阻塞线程，完成并发控制
 * 2.申请锁具有超时退出功能
 * 3.查看阻塞的线程
 * 4.可重入锁功能
 *
 * @author: mahao
 * @date: 2019/9/24
 */
public class ReentrantBooleanLock {

    private volatile boolean isLock = false;
    private final LinkedList<Thread> blockQueue = new LinkedList<>();
    private Thread lockThread = null;//持有锁的线程
    private int lockNum = 0;//重入几层，可重入锁的记录器


    /**
     * 可重入获取锁，但是不可被打断
     */
    public synchronized void lock() {
        while (isLock && (lockThread != Thread.currentThread())) {
            try {
                blockQueue.addLast(Thread.currentThread());
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (lockNum == 0) {
            isLock = true;//锁被占用,第一次请求获取锁
            lockThread = Thread.currentThread();
            blockQueue.remove(lockThread);
        }
        lockNum++;
        System.out.println(ThreadUtil.getName() + " get the lock monitor.");
    }

    public synchronized void lock(long millis) throws TimeOutException {
        if (millis <= 0)
            lock();

        long end = System.currentTimeMillis() + millis;
        while (isLock && (lockThread != Thread.currentThread())) {
            try {
                //System.out.println(end + "---" + System.currentTimeMillis());
                if (end < System.currentTimeMillis())
                    throw new TimeOutException("超时异常：timeOut " + (System.currentTimeMillis() - end));
                blockQueue.addLast(Thread.currentThread());
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (lockNum == 0) {
            isLock = true;//锁被占用,第一次请求获取锁
            blockQueue.remove((lockThread = Thread.currentThread()));
        }
        lockNum++;

    }

    //释放锁
    public synchronized void unLock() {
        if (Thread.currentThread() == lockThread) {
            lockNum--;
            if (lockNum == 0) {
                isLock = false;
                lockThread = null;
            }
            this.notifyAll();
            System.out.println(ThreadUtil.getName() + " release the lock monitor.");
        } else {
            throw new IllegalStateException("锁的非法线程释放 ...");
        }
    }

    public LinkedList<Thread> getBlockQueue() {
        return blockQueue;
    }

    public Thread getLockThread() {
        return lockThread;
    }

    public int getLockNum() {
        return lockNum;
    }
}

class TimeOutException extends Exception {

    public TimeOutException() {

    }

    public TimeOutException(String message) {
        super(message);
    }
}

class TestReentrantLock {

    final static ReentrantBooleanLock lock = new ReentrantBooleanLock();


    public static void main(String[] args) {
        Runnable task1 = () -> {
            m1();
        };

        Runnable task2 = () -> {
            m2();
        };

        new Thread(task1).start();
        new Thread(task2).start();
        while (true) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }


    }

    public static void m1() {
        try {
            lock.lock();
            System.out.println("m1 is working");
            m3();//m1方法中调用了m3方法，可重回入锁
        } finally {
            lock.unLock();
        }
    }


    public static void m2() {
        try {
            lock.lock(300);
            System.out.println("m2 is working");
        } catch (TimeOutException e) {
            e.printStackTrace();
        } finally {
            lock.unLock();
        }
    }

    public static void m3() {
        try {
            lock.lock();
            System.out.println("m3 is working");
            ThreadUtil.sleep(5000);
        } finally {
            lock.unLock();
        }
    }
}
package chapter12;

import chapter1.ThreadUtil;
import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 通过自旋 + cas算法实现自定义锁
 *
 * @author: mahao
 * @date: 2019/9/27
 */
public class MyReentrantLock implements Lock {

    //表示状态
    private volatile int status;
    private final static Unsafe unsafe;
    private final static long offset;
    private Thread lockThread;

    static {
        unsafe = ThreadUtil.getUnsafe();
        try {
            offset = unsafe.objectFieldOffset(MyReentrantLock.class.getDeclaredField("status"));
        } catch (NoSuchFieldException e) {
            throw new Error("cant get offset ");
        }
    }

    public void lock() {
        Thread currentThread = Thread.currentThread();
        for (; ; ) {
            int status = getStatus();
            if (status == 0) {
                if (compareAndSet(0, 1)) {
                    lockThread = currentThread;
                    return;
                }
            } else {
                if (lockThread == currentThread) {
                    setStatus(status + 1);
                    return;
                }
            }

        }
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
            throw new IllegalStateException();
        int sta = getStatus() - 1;
        setStatus(sta);
        if (sta == 0) {
            lockThread = null;
        }

    }

    public Condition newCondition() {
        return null;
    }

    private final boolean compareAndSet(int except, int update) {
        return unsafe.compareAndSwapInt(this, offset, status, update);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

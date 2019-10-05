package atomic.cas;

import atomic.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用cas的特性，创建一个显示锁，当线程获取不到锁的时候，快速失败。
 * atomic.compareAndSet(0, 1);通过这个去创建锁，当前值与期望值不同时，返回false，否则
 * 返回true，并且更改当前值。
 *
 * @author: mahao
 * @date: 2019/9/21
 */
public class CASLock {

    /**
     * 当等于0的时候，表示锁空闲
     */
    private final AtomicInteger atomic = new AtomicInteger(0);

    private Thread lockThread;

    /**
     * 尝试去获取锁，如果获取不到，会抛出异常，不会阻塞等待锁。
     *
     * @throws CASException
     */
    public void tryLock() throws CASException {
        /*
            compareAndSet算法的体现，通过比较expect值和本来值，如果相同，将update赋值本来值，这个方法是原子性的，
            返回结果为true。否则，如果expect值和本来值不同，则说明有其他线程已经获得过锁了，所以只能接受失败（抛出异常）。

         */
        boolean flag = atomic.compareAndSet(0, 1);
        if (flag) {
            lockThread = Thread.currentThread();
        } else {
            throw new CASException(ThreadUtil.getName() + " get lock fail ");
        }

    }

    /**
     * 释放锁的操作，将atomic的值重新变回0。
     * 因为释放锁的操作是放在finally中的，如果没有获取到锁的线程调用，则会造成锁被提前释放，
     * 真正持有锁的线程还没释放锁，其他线程又去获取锁。所以需要指定持有锁的线程去释放。
     */
    public void unLock() {
        if (atomic.get() == 0) {
            return;//已经释放过锁了，又去释放的错误操作
        }
        if (Thread.currentThread() == lockThread) {
            atomic.set(0);
        }
    }

    /**
     * 循环等待获取锁,类似addAndget()方法；
     */
    public void tryLock2() {

        while (atomic.compareAndSet(0, 1)) ;

        lockThread = Thread.currentThread();

    }
}

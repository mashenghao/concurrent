package unsafe;

import chapter3.ThreadUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 用boolean实现锁的获取
 *
 * @author: mahao
 * @date: 2019/8/13
 */
public class BooleanLock implements Lock {

    //表示锁是否被占用,false表示空闲
    private volatile boolean initValue;

    private Collection<Thread> blockThreadCollection = new ArrayList<>();

    private Thread currentThread;//当前获得锁的线程

    public BooleanLock() {
        this.initValue = false;
    }

    /**
     * 去获取锁，如果获取到锁，则让initValue设置为false；
     * 锁被占用，则让当前线程wait()等待。这样的好处是线程并发时，不用
     * synchronized方法同步，太消耗资源，用显示锁的好处，我也不知道。
     * <p>
     * <p>
     * 因为当前类是充当显示锁，所以要保证当前对象就是安全的，在并发环境下，不会
     * 出问题，要给方法加锁。原理就是，当锁被占用时，当前方法就一直处于wait循环，
     * 一直到获取到锁为止。
     *
     * @throws InterruptedException
     */
    public synchronized void lock() throws InterruptedException {
        while (initValue) {//当锁被占用，一直等待
            blockThreadCollection.add(Thread.currentThread());
            this.wait();//是让当前显式对象锁 wait
        }

        blockThreadCollection.remove((currentThread = Thread.currentThread()));
        this.initValue = true;//获取带锁后，将占用标记为在使用
        System.out.println(ThreadUtil.getName() + " get the lock monitor.");
    }

    /**
     * 带超时等待的获得锁，当线程获得锁的时间，大于指定时间，则结束锁的获取
     *
     * @param millos
     * @throws InterruptedException
     * @throws TimeOutException
     */
    public synchronized void lock(long millos) throws InterruptedException, TimeOutException {
        if (millos < 0)
            lock();

        long hasTime = millos;
        long endtime = System.currentTimeMillis() + millos;

        while (initValue) {
            if (hasTime <= 0)
                throw new TimeOutException(ThreadUtil.getName() + "TimeOutException");
            blockThreadCollection.add(Thread.currentThread());
            this.wait(hasTime);//当前锁已经被占用
            hasTime = endtime - System.currentTimeMillis();
        }

        blockThreadCollection.remove((currentThread = Thread.currentThread()));
        this.initValue = true;//获取带锁后，将占用标记为在使用
        System.out.println(ThreadUtil.getName() + " get the lock monitor.");
    }

    /**
     * 释放锁，需要将锁表示表示为未被使用，并且释放被阻塞的线程
     */
    public synchronized void unLock() {
        if (Thread.currentThread() == currentThread) {
            this.initValue = false;
            System.out.println(ThreadUtil.getName() + " release the lock monitor.");
            this.notifyAll();
        }

    }

    @Override
    public Collection<Thread> getBlockThread() {
        return Collections.unmodifiableCollection(blockThreadCollection);
    }

    public int getBlockSize() {
        return blockThreadCollection.size();
    }
}

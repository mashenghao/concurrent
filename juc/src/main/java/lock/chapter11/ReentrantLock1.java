package lock.chapter11;

import atomic.ThreadUtil;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁讲解：
 * 一个可重入互斥Lock具有与使用synchronized方法和语句访问的隐式监视锁相同的基本行为和语义，但具有扩展功能。
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class ReentrantLock1 {

    private static int i = 0;

    /**
     * 1. ReentranLock具有隐式锁的行为和语义，可以控制并发问题。
     * 2.构造方法，可以设定是否公平锁
     * public ReentrantLock(boolean fair) {
     * sync = fair ? new FairSync() : new NonfairSync();
     * }
     * 3.lockInterruptibly，线程在调用lock.lock()方法时，当获取不到锁的时候，是阻塞住的，这时候就会
     * 就会陷入等待，则可以通过设定可以中断的申请，停止过长的等待。比如t1一直等待，则t2长时间得不到，则
     * 停止等待。
     *
     * @param args
     */
    public static void main(String[] args) {

        final ReentrantLock lock = new ReentrantLock();


        Thread t1 = new Thread() {
            public void run() {
                try {
                    lock.lock();
                    i++;
                    System.out.println("Thread t1 is working ");
                    while (true) {

                    }
                } finally {
                    lock.unlock();
                }
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                try {
                    lock.lockInterruptibly();
                    i++;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        t1.start();
        t2.start();

        ThreadUtil.sleep(200);
        t1.interrupt();
        t2.interrupt();
    }

}

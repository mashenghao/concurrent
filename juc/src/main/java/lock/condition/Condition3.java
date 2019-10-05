package lock.condition;

import atomic.ThreadUtil;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: mahao
 * @date: 2019/9/26
 */
public class Condition3 {

    /**
     * t1的执行必须等到t2唤醒之后才允许去执行，否则会一直陷入阻塞中
     *
     * @param args
     */
    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("t1 get lock and begin work ..");
                    condition.await();
                    System.out.println("t1 get lock and finshed work ..");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {

                    lock.lock();
                    System.out.println("t2 get lock and begin work ..");
                    condition.signal();
                    System.out.println("t2 get lock and finshed work ..");
                } finally {
                    lock.unlock();
                }
            }
        };

        t1.start();
        ThreadUtil.sleep(1000);
        t2.start();

    }
}

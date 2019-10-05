package lock.chapter12;


import atomic.ThreadUtil;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

/**
 * @author: mahao
 * @date: 2019/9/25
 */
public class AQS1 {

    public static void main(String[] args) {
        Lock lock = new MyReentrantLock();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("A获取到锁1，在执行");
                    ThreadUtil.sleep(10000);
                    lock.lock();
                    System.out.println("A获取到锁2，在执行");
                    lock.unlock();
                } finally {
                    lock.unlock();
                }
            }
        };
        t1.start();

        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    System.out.println("B获取到锁1，在执行");
                    lock.lock();
                    System.out.println("B获取到锁2，在执行");
                    lock.unlock();
                } finally {
                    lock.unlock();
                }
            }
        };

        t2.start();
    }

    Semaphore semaphore = new Semaphore(2);
}

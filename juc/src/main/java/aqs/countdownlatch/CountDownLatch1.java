package aqs.countdownlatch;

import atomic.ThreadUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 案例：
 * 两个线程相互协作，A需要等待B线程的准备数据。
 *
 * @author: mahao
 * @date: 2019/9/22
 */
public class CountDownLatch1 {

    public static void main(String[] args) {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final CountDownLatch latch = new CountDownLatch(1);
        Thread t1 = new Thread() {
            @Override
            public void run() {
                System.out.println("thread A begin work .... ");

                try {
                    System.out.println("thread A waiting thread B .... ");
                    latch.await();//开始工作一段时间后，需要等待B线程准备的数据
                    int v1 = atomicInteger.get();
                    atomicInteger.compareAndSet(1, 2);
                    System.out.println("thread a finsh work，the " + atomicInteger.get());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                System.out.println("thread B begin prepare work .... ");
                ThreadUtil.sleep(3000);
                atomicInteger.getAndIncrement();
                latch.countDown();
                System.out.println("thread B finshed prepare work .... ");
            }
        };

        t1.start();
        t2.start();
    }
}

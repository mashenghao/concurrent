package aqs.cyclicbarrier;


import atomic.ThreadUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 使用countdown实现CyclicBarrer，让所有的子线程全部都是
 * 同一时间结，就是用countDownLatch的await方法，让子线程本身阻塞，等待其他线程完毕。
 * 问题是会造成多次的函数回调。
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class CyclicBarrier3 {

    static class MyCycli extends CountDownLatch {

        Runnable runnable;

        public MyCycli(int count, Runnable runnable) {
            super(count);
            this.runnable = runnable;
        }

        public void await() throws InterruptedException {
            super.countDown();
            super.await();
            runnable.run();
        }
    }

    public static void main(String[] args) {
        final MyCycli myCycli = new MyCycli(2, new Runnable() {
            @Override
            public void run() {
                System.out.println("回调函数");
            }
        });

        new Thread() {
            @Override
            public void run() {
                ThreadUtil.sleep(5000);
                try {
                    System.out.println("线程A在执行 ");
                    myCycli.await();
                    System.out.println("线程A 执行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                ThreadUtil.sleep(2000);
                try {
                    System.out.println("线程B在执行 ");
                    myCycli.await();
                    System.out.println("线程B 执行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

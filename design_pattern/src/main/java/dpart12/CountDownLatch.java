package dpart12;

import chapter3.ThreadUtil;

/**
 * Count Down设计模式，用于控制线程的并发数
 *
 * @author: mahao
 * @date: 2019/9/3
 */
public class CountDownLatch {

    private final int size;
    private int count;


    public CountDownLatch(int size) {
        this.size = size;
    }

    public void countDown() {
        synchronized (this) {
            count++;
            this.notifyAll();
        }
    }

    public void await() throws InterruptedException {
        synchronized (this) {
            while (size != count) {
                this.wait();
            }
        }
    }
}

class Test {
    public static void main(String[] args) throws InterruptedException {

        final CountDownLatch countDownLatch = new CountDownLatch(5);

        //线程组A，
        for (int i = 0; i < 5; i++) {
            new Thread() {
                @Override
                public void run() {
                    System.out.println(ThreadUtil.getName() + "is running");
                    countDownLatch.countDown();
                }
            }.start();
        }

        //线程组B
        countDownLatch.await();
        System.out.println("============================");
        System.out.println("线程B运行结束");

    }
}
package countDownLatch;

import chapter3_methods.ThreadUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 使用jdk并发包的类，实现一个线程等待其余线程完成任务;
 * <p>
 * 案例说明：
 * <p>
 * 线程组A，有五个线程，线程B，线程B运行需要等待线程组A运行结束才允许。
 *
 * @author: mahao
 * @date: 2019/9/3
 */
public class JDKCountDown {


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


/**
 * 自己实现
 */
class MyCountDown {

    private static int count = 5;
    private final static Object Lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        //线程组A，
        for (int i = 0; i < 5; i++) {
            new Thread() {
                @Override
                public void run() {
                    System.out.println(ThreadUtil.getName() + "is running");
                    synchronized (Lock) {
                        count--;
                        Lock.notifyAll();
                    }


                }
            }.start();
        }

        //线程组B
        synchronized (Lock) {
            while (count > 0) {
                Lock.wait();
            }
        }
        System.out.println("============================");
        System.out.println("线程B运行结束");

    }
}

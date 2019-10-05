package utils.semaphore;

import atomic.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * Semaphore操作介绍: 控制并发线程数
 * 一个计数信号量。 在概念上，信号量维持一组许可证。 如果有必要，
 * 每个acquire()都会阻塞，直到许可证可用，然后才能使用它。
 * 每个release()添加许可证，潜在地释放阻塞获取方。 但是，没有使用实际的许可证对象;
 * Semaphore只保留可用数量的计数，并相应地执行。
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class Semaphore2 {

    static final Semaphore semaphore = new Semaphore(5);

    public static void main(String[] args) {


        Thread t1 = new Thread() {

            public void run() {
                TestAcquire();
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                TestAcquire();
            }
        };

        t1.start();
        t2.start();
        ThreadUtil.sleep(10);
        t2.interrupt();
    }


    /**
     * Semaphore可以一次性申请多个许可证,
     * 当许可证的数量满足线程申请的许可数量时候，则线程执行，否则线程会陷入阻塞。
     * 线程2则要等线程1执行完毕在去执行。
     * <p>
     * 对于acquire方法，当线程为获取到足够的许可证时，线程是可以被打断的。因为线程2在等待线程1释放，自己处于
     * wait状态，对于长时间获取不到锁的线程，可以通过中断，打断获取。如果不想线程被打断，则可以通过acquireUninterruptibly()
     * 方法，申请不可中断的锁。
     */
    public static void TestAcquire() {
        try {
            System.out.println("before acquire the permits is : " + semaphore.availablePermits());
            semaphore.acquire(3);
            System.out.println(ThreadUtil.getName() + "get semaphore, " +
                    "and availablePermits is : " + semaphore.availablePermits());
            System.out.println(semaphore.getQueueLength());
            ThreadUtil.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(3);
            System.out.println("finally the permites is " + semaphore.availablePermits());
        }
    }

    /**
     * 测试尝试获取信号量中的许可证，如果许可得到，正常执行， 否则，直接返回失败，报告阻塞。
     */
    @Test
    public void TestTryAcquire() throws InterruptedException {

        final Semaphore semaphore = new Semaphore(5);

        Thread t1 = new Thread() {
            public void run() {
                try {
                    int i = semaphore.drainPermits();//申请完所有的许可证
                    System.out.println(ThreadUtil.getName() + " 得到了许可证 " + i);
                    ThreadUtil.sleep(10000);
                } finally {
                    semaphore.release(5);
                }
            }
        };

        Thread t2 = new Thread() {
            public void run() {
                if (semaphore.tryAcquire()) {
                    try {
                        System.out.println(ThreadUtil.getName());
                    } finally {
                        semaphore.release();
                    }
                } else {
                    System.out.println("没有许可证可以申请了");
                }
            }
        };

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}

package aqs.cyclicbarrier;

import atomic.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * BrokenBarrierException异常触发条件
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class CyclicBarrier2 {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        Thread t1 = new Thread() {
            @Override
            public void run() {
                ThreadUtil.sleep(1000);
                try {
                    cyclicBarrier.await();
                    System.out.println("t1 end ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {

                try {
                    ThreadUtil.sleep(2000);
                    cyclicBarrier.await();
                    System.out.println("t2 end ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();

        while (true) {
            int parts = cyclicBarrier.getParties();
            int numberWaiting = cyclicBarrier.getNumberWaiting();
            System.out.println("分成几部分： " + parts);
            System.out.println("还有多少正在等待： " + numberWaiting);
            ThreadUtil.sleep(500);
            cyclicBarrier.reset();
        }

    }


    @Test
    public void doWork() throws BrokenBarrierException, InterruptedException {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        while (true) {
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    ThreadUtil.sleep(1000);
                    try {
                        cyclicBarrier.await();
                        System.out.println("t1 end ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread t2 = new Thread() {
                @Override
                public void run() {

                    try {
                        ThreadUtil.sleep(2000);
                        cyclicBarrier.await();
                        System.out.println("t2 end ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };

            t1.start();
            t2.start();

            cyclicBarrier.await();
            cyclicBarrier.reset();

        }

    }

}

package aqs.cyclicbarrier;

import atomic.ThreadUtil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier的作用，线程之间会相互等待，知道结束；
 * 将一个任务切分多个部分，先完成的那部分会等待后完成的那部分完成。
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class CyclicBarrier1 {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        //将分成三部分
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            public void run() {
                //回调函数，当所有的部分线程都已经完成了任务，则会调用。
                System.out.println("all part finshed work ... ");

            }
        });

        Thread t1 = new Thread() {
            @Override
            public void run() {
                System.out.println("t1 start work ...");
                ThreadUtil.sleep(5000);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("t1 end work ... ");
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                System.out.println("t2 start work ...");
                ThreadUtil.sleep(3000);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("t2 end work ... ");
            }
        };

        t1.start();
        t2.start();

        cyclicBarrier.await();
        System.out.println("main end work");

    }
}

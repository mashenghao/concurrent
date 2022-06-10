package aqs.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 案例：
 * 并行处理数据，等待所有线程处理结束，总结所有
 * 让他们并且去在一个数组中，去修改数据，当都修改完成后，去
 * 打印结果。
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class CountDownLatch2 {

    public static void main(String[] args) throws InterruptedException {
        final AtomicIntegerArray array = new AtomicIntegerArray(10);
        final CountDownLatch latch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 10; i++) {//开辟是个线程，分别去最shardata做工
            executorService.submit(new MyRun(array, i, latch));
        }
        executorService.shutdown();
        //executorService.awaitTermination(1, TimeUnit.DAYS); x线程池提供的等待方法

        latch.await();
        System.out.println(array);
    }

    static class MyRun implements Runnable {

        AtomicIntegerArray array;
        int i;
        CountDownLatch latch;

        public MyRun(AtomicIntegerArray array, int i, CountDownLatch latch) {
            this.array = array;
            this.i = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            for (; ; ) {
                int current = array.get(i);
                int next = current + i;
                if (array.compareAndSet(i, current, next)) {
                    latch.countDown();
                    return;
                }

            }

        }
    }

}

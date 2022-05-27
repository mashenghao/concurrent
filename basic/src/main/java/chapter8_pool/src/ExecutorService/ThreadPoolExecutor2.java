package chapter8_pool.src.ExecutorService;

import chapter3_methods.ThreadUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池的关闭
 *
 * @author: mahao
 * @date: 2019/10/4
 */
public class ThreadPoolExecutor2 {
    /**
     * 1.corePoolSize: 线程池中保持的运行线程个数，即使空闲也不会减少，除非allowCoreThreadTimeOut设置为true
     * 2.maximumPoolSize : 线程池中最多线程个数
     * 3.keepAliveTime：时间超过这个后，将会将线程个数降到corepoolseze
     * 4.unit ： 时间单位
     * 5.threadFactory：
     * 6.handler ：拒绝策略
     * <p>
     * <p>
     * {@code corePoolSize < 0}<br>
     * {@code keepAliveTime < 0}<br>
     * {@code maximumPoolSize <= 0}<br>
     * {@code maximumPoolSize < corePoolSize}
     */
    public static void main(String[] args) throws InterruptedException {

        /*
        案例1：
        环境：设置运行的线程为1，最大个数为2，3秒队列空，线程回收，阻塞队列为2
        情况1：两个任务提交。这个情况，阻塞队列不会满，所以不会触发工作线程个数增加，
            工作线程只有1个，当任务1运行，任务2在queue中等待。
        情况2：4个任务提交，会触发线程扩充，当队列满后，就会触发工作线程触发，将线程增加到2个。
            扩容条件时： 任务队列满了线程会扩容到容纳所有的任务，但必须小于maxSize。
        情况3： 5个任务提交，会触发拒绝策略，因为最大线程个数+阻塞队列=4，<5

         */
        ThreadPoolExecutor pool =
                new ThreadPoolExecutor(1, 2, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5), new ThreadPoolExecutor.AbortPolicy());


        pool.submit(() -> {
            System.out.println(ThreadUtil.getName() + " is doing ...  1");
            ThreadUtil.sleep(15000);
        });
        pool.submit(() -> {
            System.out.println(ThreadUtil.getName() + " is doing ...  2");
            ThreadUtil.sleep(3000);
        });

        pool.submit(() -> {
            System.out.println(ThreadUtil.getName() + " is doing ...  3");
            ThreadUtil.sleep(3000);
        });
        pool.submit(() -> {
            System.out.println(ThreadUtil.getName() + " is doing ...  4");
            ThreadUtil.sleep(3000);
        });
        pool.submit(() -> {
            System.out.println(ThreadUtil.getName() + " is doing ...  5");
            ThreadUtil.sleep(10000);
        });

        //mointor(pool);

        //立即关闭，返回尚未执行的任务。
        /*List<Runnable> runnables = pool.shutdownNow();
        for (Runnable runnable : runnables) {
            System.out.println(runnable);
        }*/

        //1.关闭线程池，当其他任务执行完毕后
        pool.shutdown();
        System.out.println(pool.getQueue().size());
        //被关闭后，则不允许添加新任务
        /*pool.submit(() -> {
            System.out.println(ThreadUtil.getName() + " is doing ...  6");
            ThreadUtil.sleep(10000);
        });*/

        pool.awaitTermination(100, TimeUnit.MINUTES);


    }

    public static void mointor(ThreadPoolExecutor pool) {
        new Thread() {
            @Override
            public void run() {
                int ac = -1;
                int qs = -1;
                while (true) {
                    if (ac != pool.getActiveCount() || qs != pool.getQueue().size()) {
                        int corePoolSize = pool.getCorePoolSize();
                        int maximumPoolSize = pool.getMaximumPoolSize();
                        int activeCount = pool.getActiveCount();
                        int queueSize = pool.getQueue().size();
                        System.out.printf("-- Pool#Core:%d , Active:%d , Max:%d ,  Queue_Size:%d\n",
                                corePoolSize, activeCount, maximumPoolSize, queueSize);
                        ac = activeCount;
                        qs = queueSize;
                    }
                }
            }
        }.start();


    }
}

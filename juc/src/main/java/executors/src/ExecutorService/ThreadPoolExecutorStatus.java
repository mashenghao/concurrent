package executors.src.ExecutorService;

import atomic.ThreadUtil;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试线程池的生命周期，调用关闭方法后，
 * 是否允许新增加任务，
 * 队列中任务时候会关闭。
 *
 * @author mahao
 * @date 2022/05/26
 */
public class ThreadPoolExecutorStatus {

    public static AtomicInteger serial = new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "池中-" + serial.getAndIncrement());
            }
        });

        //1. 创建线程池，线程池执行一个任务,当线程池中有一个线程在执行时，
        pool.execute(() -> {
            System.out.println("task1 开始执行任务");
            ThreadUtil.sleep(5000);
            System.out.println(ThreadUtil.getName());
            System.out.println("task1 结束执行任务");
        });
        ThreadUtil.sleep(10000);
        System.out.println("==========================  测试1  ==============================");


        //2.调用shutdowm，不能在提交新任务了，但是task里面的还是回去执行完的。
        for (int i = 0; i < 4; i++) {
            final int fi = i;
            pool.execute(() -> {
                ThreadUtil.sleep(3000);
                System.out.println("执行中task" + fi);
            });
        }
        pool.shutdown();
        System.out.println("调用了shutdowm，但是任务还是会执行完毕后，才会去关闭。 shutdown是不阻塞的。");
        try {
            pool.submit(() -> System.out.println("关闭后再次调用，就会报错"));
        } catch (RejectedExecutionException e) {
        }
        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            //一直循环池中等待任务结束
            System.out.println("等待测试2中，线程池状态变为终止状态");
        }
        System.out.println("==========================  测试2  ==============================");

        //3.await方法，是等待shutdowm的，该方法运行结束，要么是超时，要么是线程池的状态变成（SHUT结束后）TERMINATED
        //如果方法返回值是true，则是线程池状态变为TERMINATED 如果是false，则是超时导致的，线程池状态任然不是TERMINATED状态
        pool = Executors.newFixedThreadPool(2, r -> new Thread(r, "池中-" + serial.getAndIncrement()));
        for (int i = 0; i < 4; i++) {
            final int fi = i;
            pool.execute(() -> {
                ThreadUtil.sleep(3000);
                System.out.println("执行中task" + fi);
            });
        }
        pool.shutdown();
        System.out.println("调用了shutdowm，但是任务还是会执行完毕后，才会去关闭。 shutdown是不阻塞的。");
        boolean b = pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("调用await方法，等待shutdowm任务结束变成TERMINATED状态" + b);

        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("等待测试3任务结束");
        }
        System.out.println("==========================  测试3  ==============================");

        //shutdowmNow，会立即kill掉任务，队列中的任务会返回，然后正在执行的任务也会中断执行。
        pool = Executors.newFixedThreadPool(2, r -> new Thread(r, "池中-" + serial.getAndIncrement()));
        for (int i = 0; i < 4; i++) {
            final int fi = i;
            pool.execute(() -> {
                ThreadUtil.sleep(3000);
                System.out.println("执行中task" + fi);
            });
        }
        ThreadUtil.sleep(1000);
        List<Runnable> list = pool.shutdownNow();
        System.out.println("队列中的任务" + list);

        System.out.println("main 运行结束");


    }
}

package chapter8.src.ExecutorService;

import chapter3.ThreadUtil;

import java.util.concurrent.*;

/**
 * ExecutorService APi讲解下
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class ExecutorService4 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

         /*
        当线程池设置为allowCoreThreadTimeOut=true，则会将工作线程在限定时间内不被使用，
        自动回收。
         */
        executor.setKeepAliveTime(10, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);

        ThreadUtil.mointor(executor);

        System.out.println(executor.getActiveCount());
        executor.execute(() -> System.out.println(123));

        System.out.println(executor.getActiveCount());

        ThreadUtil.sleep(1000);
        System.out.println("diaoyong ");
        int i = executor.prestartAllCoreThreads();
        System.out.println(i);

    }


}

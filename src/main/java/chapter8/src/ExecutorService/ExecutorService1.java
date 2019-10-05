package chapter8.src.ExecutorService;


import chapter3.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * shutdown中关于队列中的任务是否还会继续执行问题分析：
 * <p>
 * 分析得知，shutdown是将阻塞队列中的所有的任务都执行完毕后，才会去关闭线程的。
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class ExecutorService1 {

    public static void main(String[] args) {
        /**
         * 开启两个线程去执行五个任务，都提交完成后，会进行shutdown操作，
         *
         */
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            final int num = i;
            executorService.execute(() -> {
                ThreadUtil.randSleep(10, 1000);
                System.out.println(num);
            });
        }

        ThreadUtil.mointor(executorService);

        //  =========     线程池的关闭状态，当调用shutdown后，会进入关闭状态，不允许其他线程提交任务，等池中的
        //                 任务执行完毕后，关闭线程池，进入终结状态。
        //  =================
        System.out.println("--------- shutdown ---------");
        System.out.println("是否关闭了线程池:  " + executorService.isShutdown());
        executorService.shutdown();
        System.out.println("是否终结了线程池:  " + executorService.isTerminated());
        System.out.println("是否正在终结线程池:  " + ((ThreadPoolExecutor) executorService).isTerminating());

    }
}

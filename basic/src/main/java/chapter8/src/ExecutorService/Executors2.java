package chapter8.src.ExecutorService;

import chapter3.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @author: mahao
 * @date: 2019/10/5
 */
public class Executors2 {

    /**
     * 建一个维护足够的线程以支持给定的并行级别的线程池，并且可以使用多个队列来减少争用。
     * 并行级别对应于主动参与或可以从事任务处理的最大线程数。
     * 线程的实际数量可以动态增长和收缩。 工作窃取池不保证执行提交的任务的顺序。
     *  执行callable接口。
     * @param args
     */
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newWorkStealingPool();
        List<Future<String>> futures = new ArrayList<>(20);
        IntStream.range(0, 20).forEach((i) -> {
            Future<String> future = executorService.submit((Callable<String>) () -> {
                ThreadUtil.sleep(1000);
                return i + " ---- " + ThreadUtil.getName();
            });
            futures.add(future);
        });

        futures.forEach((future) -> {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }
}

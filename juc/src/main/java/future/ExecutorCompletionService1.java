package future;

import atomic.ThreadUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 对于future模式的优化，当多个批量task执行，返回了list的future，如何判断
 * 某个future是最先执行完毕的，是executorCompletionService的任务。
 * ExecutorCompletionService不负责执行task任务，任务执行交由传来的线程池，
 * 他是负责组织任务的执行，当执行完毕后，将执行好的添加到阻塞队列中。
 *
 * @author: mahao
 * @date: 2019/10/6
 */
public class ExecutorCompletionService1 {

    /**
     * 源码分析：
     * 本质就是FutureTask 加上 一个工作完成好的阻塞队列
     * <p>
     * 将提交的任务封装成QueueingFuture是FutureTask的子类，重写done的方法，将其改成了向阻塞队列
     * 中存放当前任务，所以执行线程结束后，会调用done方法，将future添加到，阻塞队列，去取执行好的
     * future.
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorCompletionService<String> service =
                new ExecutorCompletionService<String>(Executors.newFixedThreadPool(3));

        for (int i = 0; i < 10; i++) {
            final int num = i;
            service.submit(() -> {

                ThreadUtil.randSleep(10, 1000);
                System.out.println("--" + num);
                return num + "";
            });
        }

        Future<String> future = null;
        while ((future = service.take()) != null) {
            System.out.println(future.get());
        }
    }
}

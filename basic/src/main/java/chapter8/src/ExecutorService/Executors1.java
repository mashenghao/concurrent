package chapter8.src.ExecutorService;

import chapter3.ThreadUtil;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ExecutorService的工厂创建方式:
 * <p>
 * <p>
 * 工厂和工具方法Executor ， ExecutorService ， ScheduledExecutorService ， ThreadFactory和Callable在此包中定义的类。 该类支持以下几种方法：
 * 创建并返回一个ExecutorService设置的常用的配置设置的方法。
 * 创建并返回一个ScheduledExecutorService的方法， 其中设置了常用的配置设置。
 * 创建并返回“包装”ExecutorService的方法，通过使实现特定的方法无法访问来禁用重新配置。
 * 创建并返回将新创建的线程设置为已知状态的ThreadFactory的方法。
 * 创建并返回一个方法Callable出的其他闭包形式，这样他们就可以在需要的执行方法使用Callable
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class Executors1 {

    public static void main(String[] args) {
        /*
            1.newCachedThreadPool:
            创建一个根据需要创建新线程的线程池，但在可用时将重新使用以前构造的线程。
             这些池通常会提高执行许多短暂异步任务的程序的性能。
             调用execute将重用以前构造的线程（如果可用）。 如果没有可用的线程，
             将创建一个新的线程并将其添加到该池中。 未使用六十秒的线程将被终止并从缓存中删除。
              因此，长时间保持闲置的池将不会消耗任何资源。
              请注意，可以使用ThreadPoolExecutor构造函数创建具有相似属性但不同详细信息的池（例如，超时参数）。

              new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());

            该方法创建的线程池，是创建一个核心线程是0，最大线程个数时int最大值，当线程60s不工作时
            则会销毁线程，并且任务可以一直提交，没有上限。所以这种构建的线程池，适合工作时间短的线程，
            并且在闲置期间，不消耗资源，已经被回收了。短小任务，进入后会立即开辟线程去执行，或者由其他线程
            空余出来，但是对与长任务，则不断开辟，会消耗资源，
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                System.out.println(ThreadUtil.getName());
                ThreadUtil.sleep(100);
            });
        }
        //executorService.shutdown();
    }


    /**
     * 测试newFixedThreadPool;
     * 创建一个线程池，该线程池重用固定数量的从共享无界队列中运行的线程。
     * 在任何时候，最多nThreads线程将处于主动处理任务。
     * 如果所有线程处于活动状态时都会提交其他任务，则它们将等待队列中直到线程可用。
     * 如果任何线程由于在关闭之前的执行期间发生故障而终止，则如果需要执行后续任务，
     * 则新线程将占用它。 池中的线程将存在，直到它明确地为shutdown 。
     * <p>
     * hreadPoolExecutor(nThreads, nThreads,
     * 0L, TimeUnit.MILLISECONDS,
     * new LinkedBlockingQueue<Runnable>());
     * 不会进行线程扩展，因为核心线程数和最大线程数是相等的，任务会提交到无界队列中，需要自己关闭线程池。
     */
    @Test
    public void demo2() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                System.out.println(ThreadUtil.getName());
                ThreadUtil.sleep(1000);
            });
        }
        ThreadUtil.sleep(10000);
        executorService.shutdown();
        int size = ((ThreadPoolExecutor) executorService).getQueue().size();
        System.out.println(size);
    }

    /**
     * 只创建一个的单独工作线程。
     * 创建一个使用单个工作线程运行无界队列的执行程序，
     * 并在需要时使用提供的ThreadFactory创建一个新线程。
     * 与其他等效的newFixedThreadPool(1, threadFactory) newFixedThreadPool(1, threadFactory) ，
     * 返回的执行器保证不被重新配置以使用额外的线程。
     */
    @Test
    public void demo3() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                System.out.println(ThreadUtil.getName());
                ThreadUtil.sleep(1000);
            });
        }
        ThreadUtil.sleep(10000);
        executorService.shutdown();

    }
}

package executors.src.ExecutorService;

import atomic.ThreadUtil;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 测试尝试中断线程池中worker线程
 * 1. worker线程的run方法没有捕获任何异常，如果运行的task抛出了异常，则这个线程也抛出异常，worker线程则进入终止状态，下次
 * 则会去重新分配一个新的worker线程。
 * <p>
 * 2. 测试线程池如果是守护线程，不进行shutDown会随着main线程一起停止。
 * <p>
 * 3，对worker线程进行中断，可以发生在worker线程的两个地方：
 * 3.1: 用户task可被中断。如果用户没有处理这个中断异常，则会导致task异常，这个worker线程将会销毁状态。
 * 3.2:  worker本身可被中断只有在getTask时等待任务可以中断,但是getTask的自旋，捕获了中断异常.
 *
 * @author mahao
 * @date 2022/05/26
 */
public class ThreadPoolInterrupt {

    public static AtomicInteger atomicInteger = new AtomicInteger(1);
    public static AtomicInteger atomicInteger2 = new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2, r -> {
            Thread t = new Thread(r, "池中-" + atomicInteger.getAndIncrement());
            return t;
        });

        //每次都是开一个新的线程去执行任务。
        for (int i = 0; i < 4; i++) {
            pool.execute(() -> {
                throw new RuntimeException(ThreadUtil.getName() + "执行任务一死掉了");
            });
        }

        pool.shutdown();
        while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
        }
        System.out.println(" =================  测试1 （task异常，则worker线程也会死掉）============");


        AtomicReference<Thread> atomicReference = new AtomicReference<>();
        //测试线程池中的线程能否被中断
        ThreadPoolExecutor pool2 = (ThreadPoolExecutor) Executors.newFixedThreadPool(1, r -> new Thread(r, "池中2-" + atomicInteger2.getAndIncrement()));
        pool2.execute(() -> {
            System.out.println(ThreadUtil.getName() + Thread.currentThread().isInterrupted());
            System.out.println("中断前活跃线程数: " + pool2.getActiveCount());
            Thread.currentThread().interrupt();
            System.out.println("中断后活跃线程数: " + pool2.getActiveCount());
            System.out.println(ThreadUtil.getName() + Thread.currentThread().isInterrupted());
            atomicReference.set(Thread.currentThread());
        });
        ThreadUtil.sleep(1000);
        atomicReference.get().interrupt();

        pool2.execute(() -> {
            System.out.println(ThreadUtil.getName() + Thread.currentThread().isInterrupted());
            System.out.println("中断前活跃线程数: " + pool2.getActiveCount());
            Thread.currentThread().interrupt();
            System.out.println("中断后活跃线程数: " + pool2.getActiveCount());
            System.out.println(ThreadUtil.getName() + Thread.currentThread().isInterrupted());
        });
        System.out.println("================= 测试2 ==== （无法对worker线程进行中断, 1. worker线程运行时中断，" +
                "中断异常会发生在用户task中。 2. worker本身可被中断只有在getTask时等待任务可以中断,但是getTask的自旋，捕获了中断异常）");
        /*
           try {
                Runnable r = timed ?
                    workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                    workQueue.take();
                if (r != null)
                    return r;
                timedOut = true;
            } catch (InterruptedException retry) {
                timedOut = false;
            }
         */
    }
}

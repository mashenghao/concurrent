package executors.src.ExecutorService;



import atomic.ThreadUtil;

import java.util.concurrent.*;

/**
 * 拒绝策略: 四个
 * 1. AbortPolicy: 抛出异常
 * <p>
 * 2.CallerRunsPolicy 交由主线程运行
 * <p>
 * 3.DiscardPolicy 直接丢弃
 * <p>
 * 4.DiscardOldestPolicy 丢弃队列队头，执行这个。
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class ExecutorService3 {

    public static void main(String[] args) {
        ExecutorService executor =
                new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 0; i < 3; i++) {
            final int num = i;
            executor.execute(() -> {
                ThreadUtil.sleep(1000);
                System.out.println("任务-->" + num + "-->" + ThreadUtil.getName());
            });
        }
    }


    RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            executor.getQueue().add(r);
            System.out.println("自定义拒绝策略");
        }
    };
}

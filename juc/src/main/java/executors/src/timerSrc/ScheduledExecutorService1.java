package executors.src.timerSrc;

import atomic.ThreadUtil;

import java.util.Date;
import java.util.concurrent.*;

/**
 * juc包中的定时任务:
 * <p>
 * schedule方法创建具有各种延迟的任务，并返回可用于取消或检查执行的任务对象。
 * scheduleAtFixedRate和scheduleWithFixedDelay方法创建并执行定期运行的任务，直到取消。
 * <p>
 * 使用Executor.execute(Runnable)和ExecutorService submit方法提交的命令以请求的延迟为零进行调度。
 * 在schedule方法中也允许零和负延迟（但不是周期），并被视为立即执行的请求。
 * <p>
 * 所有schedule方法接受相对延迟和句点作为参数，而不是绝对时间或日期。
 * 这是一个简单的事情变换表示为绝对时间Date至所需的形式。 例如，要在一定的未来date ，
 * 您可以使用： schedule(task, date.getTime() - System.currentTimeMillis(),
 * TimeUnit.MILLISECONDS) 。 然而，请注意，由于网络时间同步协议，时钟漂移或其他因素，
 * 相对延迟的到期不需要与任务启用的当前Date重合。
 * <p>
 * Executors类为此包中提供的ScheduledExecutorService实现提供了方便的工厂方法。
 *
 * @author: mahao
 * @date: 2019/10/6
 */
public class ScheduledExecutorService1 {

    public static void main(String[] args) {
        ScheduledExecutorService scheduled =
                new ScheduledThreadPoolExecutor(3);
        /**
         *1. 在固定时间执行单个任务，传入的时间参数，只能是延迟执行时间，而不是具体时间，所以可以采用
         * date.getTime()-currentTime,作为延迟时间。
         * 比如在10点10分执行
         */
        ScheduledFuture<?> scheduledFuture = scheduled.schedule(() -> {
            ThreadUtil.sleep(1000);
            System.out.println("当前时间是： " + System.currentTimeMillis());
        }, new Date().getTime() - System.currentTimeMillis(), TimeUnit.SECONDS);

        ThreadUtil.sleep(1000);
        boolean cancel = scheduledFuture.cancel(true);
        System.out.println(cancel);

        /**
         * 2.定期执行任务,和timer使用是一致的。
         */

        scheduled.scheduleAtFixedRate(() -> {
            System.out.println(System.currentTimeMillis());
        }, 3, 5, TimeUnit.SECONDS);

        //线程池工具类提供的支持
        Executors.newScheduledThreadPool(3).schedule(() -> {
            System.out.println("123");
        }, 1, TimeUnit.SECONDS);



    }
}

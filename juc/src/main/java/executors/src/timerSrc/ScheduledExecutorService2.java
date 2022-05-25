package executors.src.timerSrc;

import atomic.ThreadUtil;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试定时任务线程池的定时任务类型：<br>
 *
 * <b>周期执行的任务，如果上次失败了，下次任务就不会继续执行了。</b>
 *
 * <p>
 * 1. schedule: 任务延期执行一次
 * <p>
 * 2.scheduleAtFixedRate: 任务执行距离上次任务开始执行时间间隔period
 * <p>
 * 3.scheduleWithFixedDelay: 任务执行距离上次任务结束时间间隔period
 *
 * @author mahao
 * @date 2022/05/25
 */
public class ScheduledExecutorService2 {


    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(4);

        //1.只延期执行一次
        service.schedule(() -> System.out.println("只延期执行一次"), 3, TimeUnit.SECONDS);


        /*
        //周期任务运行是，当上一个任务执行完成后，在设置下次任务执行时间。 代码是定时Task的run方法
        (ScheduledFutureTask.super.runAndReset()) {
                setNextRunTime();  //上一个任务完成后，设置task的下次执行时间为 time = time + period(上次执行时间 + 定时时间)
                reExecutePeriodic(outerTask);  //将新重置好的定时Task放入到延时队列中, ScheduledFutureTask中有获取当前时间距离执行时间的耗时。
            }

         // 提供给延时队列 DelayedWorkQueue的take方法调用，当从take中去取队首时，去调用这个task的方法，获取到剩余的调用时间。
         小于等于0，这直接执行，否则，线程陷入阻塞中，阻塞时长为剩余task时间。
         public long getDelay(TimeUnit unit) {
            return unit.convert(time - now(), NANOSECONDS);
        }
         */
        //2.固定的间隔时间执行执行,与上个任务开始时间相比，如果大于间隔时间，立即执行，否则等待执行
        AtomicInteger atomicInteger = new AtomicInteger(0);
        service.scheduleAtFixedRate(() -> {
            ThreadUtil.sleep(5000);
            System.out.println(new Date() + ": 固定延期执行(上次开始时间，5秒内执行):" + atomicInteger.getAndIncrement());
        }, 0, 3, TimeUnit.SECONDS);

        /*
        这个是延期执行，每隔多少秒执行一次。 下次任务的计算逻辑是，下个任务执行time = 上次任务的结束时间 + 间隔时间。
         、、
         time = triggerTime(-p);
         */
        service.scheduleWithFixedDelay(() -> {
            ThreadUtil.sleep(5000);
            System.out.println(new Date() + ": 固定延期执行(任务结束时间+定时周期):" + atomicInteger.getAndIncrement());
        }, 0, 3, TimeUnit.SECONDS);

        ThreadUtil.sleep(12000);

        service.shutdown();
        System.out.println("main 结束");
    }
}

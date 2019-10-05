package chapter8.src.timerSrc;

import chapter3.ThreadUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz框架使用
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class QuartzTest {

    public static void main(String[] args) throws SchedulerException {
        JobDetail jobDetail = newJob(SimpleJob.class)
                .withIdentity("job1", "group1").build();
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(cronSchedule("0/5 * * * * ?"))
                .build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    static class SimpleJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {

            //ThreadUtil.sleep(10000);
            System.out.println("------------------");
        }
    }
}



package chapter8.src.timerSrc;

import chapter3.ThreadUtil;

/**
 * 定时任务和线程的结合。
 * <p>
 * 1.Timer
 * <p>
 * 2. linux Crontab
 * <p>
 * 3.quartz 框架
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class TimerTest {

    /**
     * 分析： Timer和TimerTask的使用
     *
     * @param args
     */
    public static void main(String[] args) {

        //创建一个定时工作线程，里面有个类创建时，启动的内部线程类，是死循环一直等待定时任务
        Timer timer = new Timer();
        /*
        定时任务，是Runnable的子类，里面编写定时要执行的任务
         */
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(ThreadUtil.getName());
            }
        };
        //3.提交定时任务，将任务放在Timer的内部队列中，由执行线程执行，并设定执行规则。
        timer.schedule(task, 1000, 1000);
    }
}

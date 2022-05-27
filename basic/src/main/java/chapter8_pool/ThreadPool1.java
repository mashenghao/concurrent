package chapter8_pool;

import chapter3_methods.ThreadUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 实现线程池基本功能，使用固定线程，执行任务
 *
 * @author: mahao
 * @date: 2019/8/15
 */
public class ThreadPool1 {

    /*
    1.封装Thread对象WorkThread，使其具有生命状态;
    2.初始化创建size个WorkThread,存储到WORK_THREAD_QUEUE;
    3.创建任务（Runnable）提交接口，将任务提交到TASK_QUEUE;
     */

    private static final int DEFAULT_SIZE = 10;//默认线程中的数量
    private static long seq = 0;
    private static final String WORK_THREAD_PREFIX = "WORK_THREAD_";//默认线程中的数量

    private final int size;//线程池中的线程数量

    private final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();//任务队列
    private final List<WorkThread> WORK_THREAD_QUEUE = new ArrayList<>(DEFAULT_SIZE);//线程队列

    public ThreadPool1() {
        this(DEFAULT_SIZE);
    }

    public ThreadPool1(int size) {
        this.size = size;
        init();
    }

    //初始化创建size个工作线程，并启动
    private void init() {
        for (int i = 0; i < size; i++) {
            WorkThread wt = createWorkThread();
            wt.start();
        }
    }

    //提交任务
    public void submit(Runnable task) {

        synchronized (TASK_QUEUE) {
            // System.out.println("submit -#- add task,current TASK_QUEUE size : " + TASK_QUEUE.size() + " >> begin");
            TASK_QUEUE.addLast(task);
            TASK_QUEUE.notifyAll();//唤醒等待的workThread
            //System.out.println("submit -#- add task,current TASK_QUEUE size : " + TASK_QUEUE.size() + " >> end");
        }


    }


    //创建工作线程
    private WorkThread createWorkThread() {
        WorkThread workThread = new WorkThread(WORK_THREAD_PREFIX + (seq++));
        WORK_THREAD_QUEUE.add(workThread);
        return workThread;
    }

    private class WorkThread extends Thread {

        public Status statu = Status.FREE;//初始创建是空闲的
        private Runnable task;//工作线程负责执行的任务

        public WorkThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            //当线程未被关闭是，就一直运行
            OUTLINE:
            while (this.statu != Status.DEAD) {
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {//任务队列中为空，线程等待
                        try {
                            System.err.println(ThreadUtil.getName() + " be waiting, status: " + statu);
                            this.statu = Status.WAITING;//为以后中断线程使用，标记线程可中断，没有任务执行
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            System.err.println(ThreadUtil.getName() + " be interrupted, status: " + statu);
                            e.printStackTrace();
                            //当前线程发生中断请求，就跳转到线程循环外，判断是否线程状态是关闭了；
                            break OUTLINE;
                        }
                    }
                    //不为空，取出任务，然后设置工作线程状态
                    task = TASK_QUEUE.removeFirst();
                }

                //可以与其他线程并发执行任务，放在同步代码块之外
                if (task != null) {
                    this.statu = Status.RUNNING;
                    task.run();//运行任务
                    //运行结束后，任务值为空，线程状态置为空闲
                    this.statu = Status.FREE;
                    task = null;
                }

            }
        }

        public void close() {
            System.out.println("WorkThread.close -#- be close, current status :" + statu);
            this.statu = Status.DEAD;
        }
    }

    private enum Status {

        FREE, RUNNING, WAITING, DEAD
    }

}

class TestThreadPool1 {

    public static void main(String[] args) {
        ThreadPool1 threadPool1 = new ThreadPool1(10);
        for (int i = 0; i < 40; i++) {
            final int no = i;
            threadPool1.submit(() -> {
                System.out.println("WorkThread.run -#- is running , current WorkThread name ：" + ThreadUtil.getName() + " , task no : " + no + "  >> start");
                System.out.println("main -#- task submit ,no : " + no);
                ThreadUtil.sleep(new Random().nextInt(10) * 1000);
                System.out.println("WorkThread.run -#- is running , current WorkThread name ：" + ThreadUtil.getName() + " , task no : " + no + "  >> end");
            });
        }
    }
}
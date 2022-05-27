package chapter8_pool;

import chapter3_methods.ThreadUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 1.批次处理任务
 * 2.立即结束线程，返回为处理的task
 *
 * @author: mahao
 * @date: 2019/8/15
 */
public class ThreadPool4 extends Thread {


    //private static final int DEFAULT_SIZE = 10;//默认线程中的数量
    private static long seq = 0;
    private static final String WORK_THREAD_PREFIX = "WORK_THREAD_";//默认线程中的数量
    private static final int DEFAULT_TASK_QUEUE_LIMIT_SIZE = 100;
    public static final DiscardPolicy default_discardPolicy = (task) -> {
        throw new DiscardException("discard the submit and throw exception... ");
    };

    private int size;//线程池中的线程数量
    private final int taskQueueSize;//限制任务队列数
    private final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();//任务队列
    private final List<WorkThread> WORK_THREAD_QUEUE = new ArrayList<>();//线程队列
    private final DiscardPolicy discardPolicy;
    private boolean isClose = false;//标记线程池是否已经关闭

    private final int min;
    private int active;
    private final int max;

    public ThreadPool4() {
        this(3, 5, 10, DEFAULT_TASK_QUEUE_LIMIT_SIZE, default_discardPolicy);
    }

    /**
     * @param min           线程池中的最小数量
     * @param active        线程中的活跃数量
     * @param max           线程池中的最大数量
     * @param taskQueueSize 任务队列限制的大小
     * @param discardPolicy 拒绝策略
     */
    public ThreadPool4(int min, int active, int max, int taskQueueSize, DiscardPolicy discardPolicy) {
        this.size = size;
        this.taskQueueSize = taskQueueSize;
        this.discardPolicy = discardPolicy;
        this.min = min;
        this.active = active;
        this.max = max;

        init();

        this.start();//初始化完毕后，启动维护线程池大小的线程
    }

    //初始化创建size个工作线程，并启动
    private void init() {
        for (int i = 0; i < min; i++) {
            System.out.println("init --create -- " + i);
            WorkThread wt = createWorkThread();
            wt.start();
        }
        this.size = min;
    }

    @Override
    public void run() {
        while (!isClose) {//当线程池没被关闭

            System.out.printf("-- Pool#Min:%d , Active:%d , Max:%d; current:%d ， WORK_THREAD_QUEUE size:%d , TASK_QUEUE size:%d\n",
                    min, active, max, size, WORK_THREAD_QUEUE.size(), TASK_QUEUE.size());
            try {
                Thread.sleep(2000);
                //扩容
                if (TASK_QUEUE.size() > active && size < active) {
                    for (int i = size; i < active; i++) {
                        WorkThread w = createWorkThread();
                        w.start();
                    }
                    size = active;
                    System.out.println("--- TASK_QUEUE.size() > active  ---");
                } else if (TASK_QUEUE.size() > max && size < max) {
                    for (int i = size; i < max; i++) {
                        WorkThread w = createWorkThread();
                        w.start();
                    }
                    size = max;
                    System.out.println("---  TASK_QUEUE.size() > max  ---");
                }

                //减容
                if (TASK_QUEUE.isEmpty() && active < size) {
                    System.out.println("===========Reduce===========");
                    synchronized (TASK_QUEUE) {
                        int releaseSize = size - active;
                        for (Iterator<WorkThread> it = WORK_THREAD_QUEUE.iterator(); it.hasNext(); ) {
                            if (releaseSize <= 0)
                                break;
                            WorkThread w = it.next();
                            if (w.statu == Status.WAITING) {
                                w.close();
                                w.interrupt();
                                it.remove();
                                releaseSize--;
                            }
                            size = WORK_THREAD_QUEUE.size();

                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //提交任务
    public void submit(Runnable task) {
        if (isClose) {
            throw new IllegalStateException("Thread pool be closed ... ");
        }
        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() > taskQueueSize) {
                discardPolicy.discard(task);
            } else {
                // System.out.println("submit -#- add task,current TASK_QUEUE size : " + TASK_QUEUE.size() + " >> begin");
                TASK_QUEUE.addLast(task);
                TASK_QUEUE.notifyAll();//唤醒等待的workThread
                //System.out.println("submit -#- add task,current TASK_QUEUE size : " + TASK_QUEUE.size() + " >> end");
            }
        }
    }

    /**
     * 关闭线程池：
     * 1.中断所有没有执行任务的线程
     * 2.等待正在执行任务的线程，然后关闭
     */
    public void shutdown() throws InterruptedException {
        System.out.println("begin shutdowm thread pool ... ");
        while (!TASK_QUEUE.isEmpty()) {//等待任务队列，执行结束
            Thread.sleep(20);
        }
        int size = WORK_THREAD_QUEUE.size();
        while (size > 0) {
            for (WorkThread w : WORK_THREAD_QUEUE) {
                if (w.statu == Status.WAITING) {
                    w.interrupt();
                    w.close();
                    size--;
                } else {
                    Thread.sleep(20);
                }
            }
        }
        isClose = true;
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
                            //System.err.println(ThreadUtil.getName() + " be waiting, status: " + statu);
                            this.statu = Status.WAITING;//为以后中断线程使用，标记线程可中断，没有任务执行
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            System.err.println(ThreadUtil.getName() + " be interrupted, status: " + statu);
                            //e.printStackTrace();
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
            System.out.println("**  ** WorkThread.close -#-  " + getName() + " is closeing , current status :" + statu);
            this.statu = Status.DEAD;
        }
    }

    private enum Status {

        FREE, RUNNING, WAITING, DEAD
    }

    //拒绝策略接口
    public interface DiscardPolicy {
        void discard(Runnable task) throws DiscardException;
    }

    public static class DiscardException extends RuntimeException {
        public DiscardException(String msg) {
            super(msg);
        }
    }
}

class TestThreadPool4 {

    public static void main(String[] args) throws InterruptedException {
        ThreadPool4 threadPool4 = new ThreadPool4(3, 10, 20, 200, ThreadPool4.default_discardPolicy);

        for (int i = 0; i < 40; i++) {
            final int no = i;
            threadPool4.submit(() -> {
                System.out.println("WorkThread.run -#- is running , current WorkThread name ：" + ThreadUtil.getName() + " , task no : " + no + "  >> start");
                //System.out.println("main -#- task submit ,no : " + no);
                ThreadUtil.sleep(5_000);
                //System.out.println("WorkThread.run -#- is running , current WorkThread name ：" + ThreadUtil.getName() + " , task no : " + no + "  >> end");
            });
        }

        ThreadUtil.sleep(15_000);

        for (int i = 40; i < 100; i++) {
            final int no = i;
            threadPool4.submit(() -> {
                System.out.println("WorkThread.run -#- is running , current WorkThread name ：" + ThreadUtil.getName() + " , task no : " + no + "  >> start");
                // System.out.println("main -#- task submit ,no : " + no);
                ThreadUtil.sleep(3_000);
                // System.out.println("WorkThread.run -#- is running , current WorkThread name ：" + ThreadUtil.getName() + " , task no : " + no + "  >> end");
            });
        }

        ThreadUtil.sleep(15_000);
        threadPool4.shutdown();

        threadPool4.submit(() -> {

        });
    }
}
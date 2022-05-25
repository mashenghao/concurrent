package chapter8;

import chapter3.ThreadUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 实现线程池的拒绝策略，以及停止线程池
 *
 * @author: mahao
 * @date: 2019/8/15
 */
public class ThreadPool2 {
     /*
    拒绝策略：
        1.在提交任务的接口，检测线程池中任务队列的个数，大于限制，就不允许添加
    停止线程：
        1.创建线程池对外暴露接口，外部调用
        2.在关闭接口中，遍历工作线程，中断workThread线程
     */

    private static final int DEFAULT_SIZE = 10;//默认线程中的数量
    private static long seq = 0;
    private static final String WORK_THREAD_PREFIX = "WORK_THREAD_";//默认线程中的数量
    private static final int DEFAULT_TASK_QUEUE_LIMIT_SIZE = 100;
    public static final DiscardPolicy default_discardPolicy = (task) -> {
        throw new DiscardException("discard the submit and throw exception... ");
    };

    private final int size;//线程池中的线程数量
    private final int taskQueueSize;//限制任务队列数
    private final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();//任务队列
    private final List<WorkThread> WORK_THREAD_QUEUE = new ArrayList<>(DEFAULT_SIZE);//线程队列
    private final DiscardPolicy discardPolicy;
    private boolean isClose = false;

    public ThreadPool2() {
        this(DEFAULT_SIZE, DEFAULT_TASK_QUEUE_LIMIT_SIZE, default_discardPolicy);
    }

    /**
     * @param size          执行线程的数量
     * @param taskQueueSize 任务队列限制的大小
     * @param discardPolicy 拒绝策略
     */
    public ThreadPool2(int size, int taskQueueSize, DiscardPolicy discardPolicy) {
        this.size = size;
        this.taskQueueSize = taskQueueSize;
        this.discardPolicy = discardPolicy;
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
                            System.err.println(ThreadUtil.getName() + " be waiting, status: " + statu);
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

class TestThreadPool2 {

    public static void main(String[] args) throws InterruptedException {
        ThreadPool2 threadPool2 = new ThreadPool2(10, 40, ThreadPool2.default_discardPolicy);

        for (int i = 0; i < 40; i++) {
            final int no = i;
            threadPool2.submit(() -> {
                System.out.println("WorkThread.run -#- is running , current WorkThread name ：" + ThreadUtil.getName() + " , task no : " + no + "  >> start");
                System.out.println("main -#- task submit ,no : " + no);
                ThreadUtil.sleep(3_000);
                System.out.println("WorkThread.run -#- is running , current WorkThread name ：" + ThreadUtil.getName() + " , task no : " + no + "  >> end");
            });
        }
        ThreadUtil.sleep(10000);

        threadPool2.shutdown();

        threadPool2.submit(() -> {
        });
    }
}

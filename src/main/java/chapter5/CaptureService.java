package chapter5;


import chapter3.ThreadUtil;
import com.sun.org.apache.regexp.internal.RE;
import dpart3.Listener;
import jdk.nashorn.internal.ir.ReturnNode;
import jdk.nashorn.internal.ir.WithNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 模拟实现控制线程的并发数
 *
 * @author: mahao
 * @date: 2019/8/13
 */
public class CaptureService {

    private static List<Control> CONTROLS = new ArrayList<>();

    public static void main(String[] args) {


        Random random = new Random();
        List<Thread> worker = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            try {
                Thread t = CaptureService.createThread("线程" + i);
                t.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static Thread createThread(String name) {
        return new Thread(() -> {
            // System.out.println(ThreadUtil.getName() + "... had created ... ");

            synchronized (CONTROLS) {
                while (CONTROLS.size() >= 3) {//当已经运行的线程数大于3了，线程等待
                    try {
                        CONTROLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                CONTROLS.add(new Control());//线程中的数量允许执行，需要放在同步方法中，串行执行
            }

            //   ***********          用户方法开始（可抽象为模版方法模式）      ***********
            System.out.println(ThreadUtil.getName() + " ... running ...start ");
            ThreadUtil.sleep(new Random().nextInt(10));
            System.out.println(ThreadUtil.getName() + " ... running ... end ");
            //   ***********          用户方法结束 （并行执行方法）     ***********

            synchronized (CONTROLS) { // 线程执行结束了，释放当前线程，唤醒其他阻塞线程
                CONTROLS.remove(0);
                CONTROLS.notifyAll();
            }
        }, name);
    }

    private static class Control {

        // 控制类，可以添加一些线程控制的属性

    }
}


/********************重构线程并发数量的控制，将业务逻辑与并发数量控制解耦出来*************************/
//缺点，无法实现关闭执行线程，必须等待都执行结束。

//业务逻辑
interface Task extends Runnable {

}

class CaptureService2 {

    LinkedList<Control> CONTROLS = new LinkedList<>();
    private final int size;

    public CaptureService2(int size) {
        this.size = size;
    }

    public CaptureService2() {
        this.size = 3;
    }

    public Thread submit(Runnable task) {

        Thread t = new Thread() {
            @Override
            public void run() {
                Control control = null;
                //串行执行判断是否运行的线程大于指定个数
                synchronized (CONTROLS) {
                    while (CONTROLS.size() >= 3) {
                        try {
                            CONTROLS.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.out.println("程序被中断");
                            return;
                        }
                    }
                    CONTROLS.addLast(control = new Control(Thread.currentThread()));
                }

                //   ***********          用户方法开始      ***********
                System.out.println(ThreadUtil.getName() + " ... running ...start ");
                task.run();
                System.out.println(ThreadUtil.getName() + " ... running ... end ");
                //   ***********          用户方法结束 （并行执行方法）     ***********

                //串行执行释放当前线程至执行的标记
                synchronized (CONTROLS) {
                    CONTROLS.remove(control);
                    CONTROLS.notifyAll();
                }
            }
        };
        t.start();
        return t;
    }

    private static class Control {

        Thread thread;

        public Control(Thread thread) {
            this.thread = thread;
        }
    }

    public static void main(String[] args) {

        CaptureService2 service = new CaptureService2();
        for (int i = 0; i < 10; i++) {
            try {
                service.submit(() -> {
                    ThreadUtil.sleep(3000);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

/****************重构二：实现逻辑任务解耦分离，执行线程可以关闭未执行的任务，******************************/
//逻辑任务使用Task接口

class Service3 {

    //用于存储提交的逻辑任务线程
    volatile LinkedList<Thread> threads = new LinkedList<>();
    private volatile int count = 0;//控制并发数量
    private Thread executeThread = null;//执行线程

    //添加任务
    public void add(Task task) {
        synchronized (threads) {
            //将用户提交的任务，封装成线程
            threads.addLast(new Thread() {
                @Override
                public void run() {
                    task.run();
                    //任务执行结束，需要唤醒executeThread去执行任务
                    synchronized (threads) {
                        count--;
                        threads.notifyAll();
                    }
                }
            });
        }
    }

    /*
    执行任务，因为要具备中断的功能，执行功能必须单独放在一个
    执行线程中，否则会阻塞住调用了execute()方法的线程。
     */
    public void execute() {
        executeThread = Thread.currentThread();
        while (true) {
            synchronized (threads) {
                while (count >= 3 || threads.isEmpty()) {
                    try {
                        threads.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("中断程序执行，执行结束.....");
                        return;
                    }
                }
                count++;
                Thread t = threads.removeFirst();
                t.start();
            }
        }
    }

    public void shutdown() {
        System.out.println("调用中断方法");
        executeThread.interrupt();
    }

    public static void main(String[] args) {
        Service3 service = new Service3();

        for (int i = 0; i < 10; i++) {
            final int a = i;
            service.add(() -> {
                ThreadUtil.sleep(3 * 1000);
                System.out.println(ThreadUtil.getName() + "---- 我做工完成了");
            });
        }
        new Thread(() -> {
            while (!service.threads.isEmpty()) {

            }
            service.shutdown();
        }).start();
        service.execute();


    }
}
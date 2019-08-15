package chapter5;


import chapter3.ThreadUtil;

import java.util.ArrayList;
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

package chapter3;

import java.awt.*;

/**
 * 优雅的结束线程
 *
 * @author: mahao
 * @date: 2019/8/11
 */
public class EEndThread {

    public static void main(String[] args) {
        Work1 work1 = new Work1();
        work1.start();

        ThreadUtil.sleep(5_000);//主线程等待5s后，关闭子线程
        work1.shutDown();

        Work2 work2 = new Work2();
        work2.start();

        ThreadUtil.sleep(5_000);//主线程等待5s后，关闭子线程
        work2.interrupt();
    }
}

/**
 * 结束方式1 ：  设置开关
 * <p>
 * ，给线程设置结束标记位，判断结束标记结束线程
 */
class Work1 extends Thread {

    private boolean shutDown = false;

    @Override
    public void run() {
        while (!shutDown) { //没有关闭时，是一直工作，直至关闭
            //do work
            System.out.println("do something...");
            ThreadUtil.sleep(1000);
        }
        shutDown = false;//恢复
    }

    public void shutDown() {
        shutDown = true;
    }
}

/**
 * 结束方式二
 */
class Work2 extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("结束操作");
                break;//在这里通过外部调用interrupt，中断线程，在拦截里面做后续操作

            }

        }
    }
}
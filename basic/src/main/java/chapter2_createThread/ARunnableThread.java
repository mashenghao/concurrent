package chapter2_createThread;

import org.junit.Test;

/**
 * 用Runnable实现多线程，好处
 * <p>
 * 单继承的坏处
 * 多个线程可以使用同一个成员变量
 *
 * @author: mahao
 * @date: 2019/8/9
 */
public class ARunnableThread {

    //用Thread实现变量共享，由于线程不是同一个对象
    //需要声明为static
    @Test
    public void test1() {
        for (int i = 1; i <= 3; i++) {
            new MyThread("窗口" + i).start();
        }
    }

    //用Runnable实现变量共享
    @Test
    public void test2() {
        MyRunnable runnable = new MyRunnable();
        for (int i = 1; i <= 3; i++) {
            new  Thread(runnable,"窗口" + i).start();
        }

    }
}

//通过Thread实现多线程售票
class MyThread extends Thread {

    private static int trick = 100;

    public MyThread(String s) {
        super(s);
    }

    @Override
    public void run() {
        while (trick > 0) {
            System.out.println(Thread.currentThread().getName() + "---->" + trick);
            trick--;
        }
    }
}

class MyRunnable implements Runnable{

    private int trick = 100;

    @Override
    public void run() {
        while (trick > 0) {
            System.out.println(Thread.currentThread().getName() + "---->" + trick);
            trick--;
        }
    }
}
package chapter4;

import chapter3.ThreadUtil;
import com.sun.javafx.image.impl.ByteRgb;

/**
 * 线程并发问题
 *
 * @author: mahao
 * @date: 2019/8/12
 */
public class AThreadProblem {

    public static void main(String[] args) {
        MyThread task = new MyThread();
        for (int i = 0; i < 3; i++) {
            new Thread(task).start();
        }
    }
}

class MyThread implements Runnable {

    private int count = 100;
    private final byte[] lock = new byte[1];


    @Override
    public void run() {
        while (count > 0) {
            synchronized (lock) {//方法同步，同步失败，因为count判断任然会进来
                System.out.println(Thread.currentThread().getName() + "----->" + count--);
                ThreadUtil.sleep(10);
            }
        }
    }
}
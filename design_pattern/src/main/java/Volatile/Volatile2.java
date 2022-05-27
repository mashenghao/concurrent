package Volatile;

import chapter3_methods.ThreadUtil;

/**
 * 多线程下，读线程和写线程的内存间交互操作。
 * <p>
 * 读进程会认为变量不需要更新操作，所以，只在工作内存中，获取数据，不从主存获取数据
 * 写进程涉及数据的写操作，会进行数据刷新到主内存中。
 *
 * @author: mahao
 * @date: 2019/8/21
 */
public class Volatile2 {

    private static int count = 0;

    public static void main(String[] args) {
        ReadThread r = new ReadThread();
        WriterThread w = new WriterThread();
        r.start();
        w.start();

    }

    static class ReadThread extends Thread {
        @Override
        public void run() {
            int local = count;
            while (count < 5) {
                if (count != local) {
                    System.out.println("read - " + count);
                    local = count;
                }
            }
        }
    }

    static class WriterThread extends Thread {
        @Override
        public void run() {
            while (count < 5) {
                System.out.println("writer - " + (++count));
                ThreadUtil.sleep(1000);
            }
        }
    }
}

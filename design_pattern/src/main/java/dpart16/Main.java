package dpart16;

import chapter3_methods.ThreadUtil;

/**
 * @author: mahao
 * @date: 2019/9/5
 */
public class Main {


    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        ThreadUtil.sleep(10000);
        myThread.gc();

    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {

                gc();
                ThreadUtil.sleep(1000);
            }
        }

        public void gc() {
            System.out.println("gc..." + ThreadUtil.getName());
        }
    }
}

package chapter11;

import chapter1.ThreadUtil;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: mahao
 * @date: 2019/9/23
 */
public class ProducterConsumer {

    private static final int MAX = 10;
    private static final Lock lock = new ReentrantLock();


    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<String>();
        Producter p = new Producter(list, MAX);
        Comsumer c = new Comsumer(list, MAX);

        new Thread(p).start();
        new Thread(p).start();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();


    }


    static class Producter implements Runnable {

        LinkedList<String> list;
        int max = 0;

        public Producter(LinkedList<String> list, int max) {
            this.list = list;
            this.max = max;
        }

        public void run() {
            while (true) {
                synchronized (list) {
                    while (list.size() == max) {
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    list.addLast("s");
                    System.out.println("Producter : " + "s" + "size: " + list.size());
                    list.notifyAll();
                    ThreadUtil.sleep(1000);
                }
            }
        }

    }

    static class Comsumer implements Runnable {

        LinkedList<String> list;
        int max = 0;

        public Comsumer(LinkedList<String> list, int max) {
            this.list = list;
            this.max = max;
        }

        public void run() {
            while (true) {
                synchronized (list) {
                    while (list.size() == 0) {
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String s = list.removeFirst();
                    System.out.println("consumer : " + s + "size: " + list.size());
                    list.notifyAll();
                    ThreadUtil.sleep(1000);
                }
            }
        }

    }
}

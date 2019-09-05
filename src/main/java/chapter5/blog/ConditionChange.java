package chapter5.blog;


import chapter3.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 使用wait/notify存在的威胁：
 * <p>
 *
 * @author: mahao
 * @date: 2019/8/13
 */
public class ConditionChange {

    //生产者只负责生产一次，消费者只消费一次，当消费者线程有两个是c1和c2，则
    //当c1消费，无产品，阻塞，c2消费，无产品阻塞，p1生产，唤醒线程c1，c2，c1抢到
    //执行权，在wait代码后接着执行，执行完毕，消费，只后c2消费，c2也是wait后消费，
    //则会发生错误，条件已经改变了。
    public static void main(String[] args) {
        Producter p1 = new Producter();
        Consumer c1 = new Consumer();
        Consumer c2 = new Consumer();
        c1.start();
       c2.start();
        p1.start();
        /*
        Thread-1--调用wait方法
        Thread-2--调用wait方法
        111
        Thread-2--结束wait方法
        Thread-1--结束wait方法
         */


    }


    private static byte[] lock = new byte[1];
    private static List<Integer> list = new ArrayList<>();

    static class Producter extends Thread {
        public void run() {
            synchronized (lock) {
                System.out.println(111);
                list.add(new Random(10).nextInt());
                lock.notifyAll();
            }
        }
    }

    static class Consumer extends Thread {
        public void run() {
            synchronized (lock) {
                while (list.isEmpty()) { //解决方案，if更改为while，让wait结束后，重新判断条件
                    System.out.println(ThreadUtil.getName() + "--调用wait方法");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ThreadUtil.getName() + "--结束wait方法");
                }
                list.remove(0);
                lock.notifyAll();
            }
        }
    }
}

package chapter3;


import chapter1.ThreadUtil;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * cas带来的问题ABA问题:
 * 当A B两个线程共同工作与atomic原子对象，如果
 * A线程获取执行权， current=a,之后，时间片到期
 * B线程获取执行权，此时 current=a，然后，B线程给他赋值为b，之后B线程又赋值为a，
 * 这个时候A获取执行权，则会出现问题，此时atomic值中的a已经和A线程中的a是同一个含义了，就会出现问题了。
 *
 * @author: mahao
 * @date: 2019/9/22
 */
public class CASProblem {

    public static void main(String[] args) {
        final String a = "a";
        final String b = "b";

        final AtomicReference<String> atomic = new AtomicReference<String>("a");
        /*
        执行顺序要做到，t1 t2两个线程都拿到current = a , 然后t1线程做完a->b->a的工作后，
        t2线程再去执行，看是否成功
         */

        Thread t2 = new Thread() {
            @Override
            public void run() {
                //t2先执行，获取到current后，睡眠等待t2执行完毕。
                String s = atomic.get();
                ThreadUtil.sleep(3000);//
                System.out.println("睡眠前获取的结果是 " + s);
                boolean b = atomic.compareAndSet(s, "b");
                System.out.println("b 的结果" + b); //true，则说明有问题

            }
        };
        Thread t1 = new Thread() {
            @Override
            public void run() {
                ThreadUtil.sleep(1000);//保证t2先获取到值
                boolean b1 = atomic.compareAndSet("a", "b");
                System.out.println("b1的结果 此时值是 b" + b1);

                //在将B换成a
                boolean b2 = atomic.compareAndSet("b", "a");
                System.out.println("b2的结果 此时值是 a" + b2);
            }
        };

        t2.start();
        t1.start();

    }

}

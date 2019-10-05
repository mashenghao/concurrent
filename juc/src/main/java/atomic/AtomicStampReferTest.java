package atomic;

import atomic.ThreadUtil;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 解决cas算法的问题，用时间戳概念，乐观锁的实现。
 * 对于每次修改，时间戳会发生变化，即使两个对象相同，时间戳不同，也无法修改成功。
 *
 * @author: mahao
 * @date: 2019/9/22
 */
public class AtomicStampReferTest {

    public static void main(String[] args) {

       /* AtomicStampedReference<String> atomic = new AtomicStampedReference<String>("a", 0);
        String current = atomic.getReference();
        String next = "b";
        boolean flag = atomic.compareAndSet(current, next, 0, 1);
        System.out.println(flag);*/

        final String a = "a";
        final String b = "b";

        final AtomicStampedReference<String> atomic = new AtomicStampedReference<String>(a, 0);

         /*
        执行顺序要做到，t1 t2两个线程都拿到current = a , 然后t1线程做完a->b->a的工作后，
        t2线程再去执行，看是否成功
         */

        Thread t2 = new Thread() {
            @Override
            public void run() {
                //t2先执行，获取到current后，睡眠等待t2执行完毕。
                String s = atomic.getReference();
                int stamp = atomic.getStamp();
                ThreadUtil.sleep(3000);//
                System.out.println("睡眠前获取的结果是 " + s);
                boolean b = atomic.compareAndSet(s, "b", stamp, atomic.getStamp() + 1);
                System.out.println("b 的结果" + b); //false,则此时结果已经不相同了。

            }
        };
        Thread t1 = new Thread() {
            @Override
            public void run() {
                ThreadUtil.sleep(1000);//保证t2先获取到值
                boolean b1 = atomic.compareAndSet("a", "b", atomic.getStamp(), atomic.getStamp() + 1);
                System.out.println("b1的结果 此时值是 b" + b1);

                //在将B换成a
                boolean b2 = atomic.compareAndSet("b", "a", atomic.getStamp(), atomic.getStamp() + 1);
                System.out.println("b2的结果 此时值是 a" + b2);
            }
        };

        t2.start();
        t1.start();


    }


}

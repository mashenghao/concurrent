package atomic;


import atomic.ThreadUtil;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger来替换volatile关键字无法解决的原子性问题；
 * <p>
 * 案例:
 * 对用volatile修饰的变量并发修改，会发生并发问题。
 *
 * @author: mahao
 * @date: 2019/9/21
 */
public class AtomicIntegerTest {

    private volatile static int count = 0;
    private static AtomicInteger value = new AtomicInteger();


    public static void main(String[] args) {
        /*
         * 分析得知，volatile只能保证有序性和可见性，通过禁止指令重拍序，来保证
         * 执行的有序性， 通过向其他本地内存发送通知，让副本失效，保证每次更改是可见的，
         * 但无法保障原子性。
         */
        final Set<Integer> set = Collections.synchronizedSet(new TreeSet<Integer>());
        for (int i = 0; i < 3; i++) {
            new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        count++;
                        set.add(count);
                        System.out.println(ThreadUtil.getName() + "---" + count);
                        ThreadUtil.sleep(10);
                    }
                }
            }.start();
        }
        ThreadUtil.sleep(5000);
        System.out.println(set.size());


        /*
        使用AtomicInteger来保证原子性
         */
        final Set<Integer> set2 = Collections.synchronizedSet(new TreeSet<Integer>());
        for (int i = 0; i < 3; i++) {
            new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        int v = value.getAndIncrement();
                        set2.add(v);
                        System.out.println(ThreadUtil.getName() + "---" + v);
                        ThreadUtil.sleep(10);
                    }
                }
            }.start();
        }
        ThreadUtil.sleep(5000);
        System.out.println(set2.size());
    }
}

package chapter1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 详细介绍
 *
 * @author: mahao
 * @date: 2019/9/21
 */
public class AtomicIntegerTest2 {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        //获取值
        int v1 = atomicInteger.get();
        System.out.println(v1);
        atomicInteger = new AtomicInteger(2);
        System.out.println(atomicInteger.get());
        int v3 = atomicInteger.getAndSet(10);
        System.out.println(v3);

        /*
        getAndSet的使用：通过比较，如果符合期望值，则进行值变更
        public final int getAndSet(int newValue) {
            for (;;) {
                int current = get();
                if (compareAndSet(current, newValue))
                    return current;
            }
        }
         */

        /*
        A B线程分别调用getAndSet方法，进行数值的增加
         */
        final AtomicInteger atomic = new AtomicInteger(0);
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    int v1 = atomic.addAndGet(1);
                    ThreadUtil.sleep(10);
                    int v2 = atomic.get();
                    System.out.println(ThreadUtil.getName() + "__" + v1 + "    " + v2);
                }

            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                for (int j = 0; j < 10; j++) {
                    int v1 = atomic.addAndGet(1);
                    int v2 = atomic.get();
                    System.out.println(ThreadUtil.getName() + "__" + v1 + "    " + v2);
                }

            }
        }.start();
    }
}

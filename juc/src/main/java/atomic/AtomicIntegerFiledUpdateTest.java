package atomic;

import atomic.ThreadUtil;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * AtomicIntegerFieldUpdater可以使类对象里面的属性操作具有原子性，
 *
 * @author: mahao
 * @date: 2019/9/22
 */
public class AtomicIntegerFiledUpdateTest {


    public static void main(final String[] args) {
        //基本类型
        final AtomicIntegerFieldUpdater<In> atomicIntegerFieldUpdater =
                AtomicIntegerFieldUpdater.newUpdater(In.class, "i");
        final In in = new In();
        for (int i = 0; i < 3; i++) {
            new Thread() {//三个线程并发操作，保证结果是原子性的。
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        ThreadUtil.sleep(10);
                        int i1 = atomicIntegerFieldUpdater.addAndGet(in, 1);
                        System.out.println(i1);
                    }
                }
            }.start();
        }
        //引用类型
        AtomicReferenceFieldUpdater<In, Integer> atomicReferenceFieldUpdater =
                AtomicReferenceFieldUpdater.newUpdater(In.class, Integer.class, "integer");
        boolean b = atomicReferenceFieldUpdater.compareAndSet(in, null, 0);
        System.out.println(b);//返回结果是true

    }

    static class In {
        /**
         * 1.被修饰的属性，必须是volatile
         * 2.必须是可以被使用类访问
         * 3类型必须一致.
         */
        volatile int i;

        volatile Integer integer;

    }
}

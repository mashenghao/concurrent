package atomic;

import atomic.ThreadUtil;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author: mahao
 * @date: 2019/9/22
 */
public class AtomicArray {

    public static void main(final String[] args) {
        //创建一个原子类型的整形数组
        final AtomicIntegerArray atomicArr = new AtomicIntegerArray(10);

        //对数组元素并发操作,两个线程并发操作下标为5的元素
        new Thread() {
            @Override
            public void run() {
                for (int j = 0; j < atomicArr.length(); j++) {
                    for (int i = 0; i < 10; i++) {
                        int i1 = atomicArr.addAndGet(j, 1);
                        System.out.println("t1  " + i1);
                    }
                }

            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                for (int j = 0; j < atomicArr.length(); j++) {
                    for (int i = 0; i < 10; i++) {
                        int i1 = atomicArr.addAndGet(j, 1);
                        System.out.println("t2  " + i1);
                    }
                }
            }
        }.start();
        ThreadUtil.sleep(1000);
        System.out.println(atomicArr);
    }

}

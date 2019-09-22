package chapter5;

import chapter1.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicIntegerFiledUpdate使用场景：将普通类的属性具有原子操作。
 * <p>
 * 使用AtomicIntegerFiledUpdate使元素具有原子性。
 * 同样可以使用具有原子操作的原子类去替换原始属性，但是这样浪费空间，
 * 如果要是过多元素，则会是内存占用太多。
 *
 * @author: mahao
 * @date: 2019/9/22
 */
public class AtomicInt {

    private volatile int i;

    private AtomicIntegerFieldUpdater atomic =
            AtomicIntegerFieldUpdater.newUpdater(AtomicInt.class, "i");

    public int getI() {
        return i;
    }

    /**
     * 改写并发赋值，可以通过加synchronized的方法使其具有原子性，
     * 也可以使用AtomicIntegerFieldUpdater进行操作
     *
     * @param i
     */
    public int getAndSetint(int i) {
        return atomic.getAndAdd(this, i);
    }

    // ==================================================
    //对于属性的原子操作，同样可以使用原子类
    private AtomicInteger integer = new AtomicInteger(0);

    public int getAndSet(int i) {
        return integer.getAndSet(i);
    }

    //测试
    public static void main(String[] args) {

        final AtomicInt atomicInt = new AtomicInt();
        for (int i = 0; i < 3; i++) {
            new Thread() {//三个线程并发操作，保证结果是原子性的。
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        ThreadUtil.sleep(10);
                        int i1 = atomicInt.getAndSetint(1);
                        System.out.println(i1);
                    }
                }
            }.start();
        }
    }
}

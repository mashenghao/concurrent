package atomic.myAtomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 重写Integer类
 *
 * @author: mahao
 * @date: 2019/9/21
 */
public class CASInteger {

    private static Unsafe unsafe;

    static {
        Field singleoneInstanceField = null;
        try {
            singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            unsafe = (Unsafe) singleoneInstanceField.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }


    //对象值
    private volatile int value;

    public int getAndChange(int num) {
        for (; ; ) {
            int current = getValue();
            int next = current + num;
            if (compareAndSet(current, next))
                return next;
        }
    }

    public int getAndAdd() {
        return getAndChange(1);
    }

    public int getAndDecle() {
        return getAndChange(-1);
    }

    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

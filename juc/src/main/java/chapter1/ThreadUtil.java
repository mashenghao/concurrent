package chapter1;

import chapter6.UnsafeTest;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author: mahao
 * @date: 2019/9/21
 */
public class ThreadUtil {

    public static String getName() {
        return Thread.currentThread().getName() + " ";
    }

    public static void sleep(long l) {

        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }

    public static void execute(Runnable task) {
        new Thread(task).start();
    }
}

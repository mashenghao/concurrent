package atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * @author: mahao
 * @date: 2019/9/21
 */
public class ThreadUtil {

    private final static Random random = new Random();


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

    /**
     * @param
     */
    public static void randSleep(int num, int s) {
        sleep(random.nextInt(num) * s);
    }
    
}

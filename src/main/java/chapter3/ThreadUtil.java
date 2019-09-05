package chapter3;

/**
 * @author: mahao
 * @date: 2019/8/10
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



    public static void execute(Runnable task) {
        new Thread(task).start();
    }
}

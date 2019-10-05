package chapter3;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: mahao
 * @date: 2019/8/10
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

    public static void randSleep(int num, int s) {
        sleep(random.nextInt(num) * s);
    }

    public static void execute(Runnable task) {
        new Thread(task).start();
    }

    public static void mointor(ExecutorService es) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                int ac = -1;
                int qs = -1;
                ThreadPoolExecutor pool;
                if (es instanceof ThreadPoolExecutor)
                    pool = (ThreadPoolExecutor) es;
                else {
                    throw new ClassCastException();
                }
                while (true) {
                    if (ac != pool.getActiveCount() || qs != pool.getQueue().size()) {
                        int corePoolSize = pool.getCorePoolSize();
                        int maximumPoolSize = pool.getMaximumPoolSize();
                        int activeCount = pool.getActiveCount();
                        int queueSize = pool.getQueue().size();
                        System.out.printf("-- Pool#Core:%d , Active:%d , Max:%d ,  Queue_Size:%d\n",
                                corePoolSize, activeCount, maximumPoolSize, queueSize);
                        ac = activeCount;
                        qs = queueSize;
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();

    }
}

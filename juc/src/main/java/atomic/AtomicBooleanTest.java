package atomic;

import atomic.ThreadUtil;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AtomicBoolean使用场景：
 * 替代volatile boolean 用于结束线程
 *
 * @author: mahao
 * @date: 2019/9/21
 */
public class AtomicBooleanTest {

    private final static AtomicBoolean ATOMIC_BOOLEAN = new AtomicBoolean(true);


    public static void main(String[] args) {

        new Thread() {
            @Override
            public void run() {
                while (ATOMIC_BOOLEAN.get()) {
                    ThreadUtil.sleep(1000);
                    System.out.println("work ... ");
                }
            }
        }.start();

        ThreadUtil.sleep(5000);
        ATOMIC_BOOLEAN.getAndSet(false);
    }
}

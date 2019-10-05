package atomic.cas;

import atomic.ThreadUtil;

/**
 * 测试
 *
 * @author: mahao
 * @date: 2019/9/21
 */
public class CASLockTest {

    private final static CASLock LOCK = new CASLock();

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        doWork2();
                    } catch (CASException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * synchronize会阻塞住为获取到锁的线程，知道锁空闲
     */
    public static void doWork() {
        synchronized (CASLockTest.class) {
            System.out.println(ThreadUtil.getName() + ".....");
            ThreadUtil.sleep(100000);

        }

    }

    /**
     * 当没有获取到锁，会快速的抛出异常，不会阻塞线程
     */
    public static void doWork2() throws CASException {
        try {
            LOCK.tryLock2();
            System.out.println(ThreadUtil.getName() + ".....");
            ThreadUtil.sleep(1000);
        } finally {
            LOCK.unLock();
        }


    }
}

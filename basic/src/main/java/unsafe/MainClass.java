package unsafe;

import chapter3_methods.ThreadUtil;

/**
 * 测试显式锁
 *
 * @author: mahao
 * @date: 2019/8/13
 */
public class MainClass {

    private static int i = 0;
    private static Object obj = new Object();

    public static void main(String[] args) {

        BooleanLock lock = new BooleanLock();

        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 5; j++) {
                try {
                    /*
                       执行逻辑就是，lock中获取Boolean对象锁，获取不到，一直等待，
                       获取到了对象锁，就将标识为置为在使用。
                     */
                    lock.lock();
                    int t = i;
                    i = t + 1;
                    System.out.println("1---> " + i);
                    ThreadUtil.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unLock();//最后释放锁
                }


            }

        });
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 5; j++) {
                try {
                    lock.lock();
                    int t = i;
                    i = t + 1;
                    System.out.println("2---> " + i);
                    ThreadUtil.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unLock();
                }
            }

        });
        t1.start();

        t2.start();

        ThreadUtil.sleep(1000);
        lock.unLock();
    }
}

class MainClass2 {

    public static void main(String[] args) {
        BooleanLock lock = new BooleanLock();

        for (int i = 0; i < 4; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        lock.lock();
                        work();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unLock();
                    }
                }
            }.start();
        }

    }


    private static void work() {
        ThreadUtil.sleep(3000);
        System.out.println(ThreadUtil.getName() + " is working .....");
    }
}

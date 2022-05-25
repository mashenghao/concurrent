package chapter5.blog;

/**
 * 解决notify早期通知：
 * <p>
 * <p>
 * 通过设置一个表示数据，用来表示线程，
 * 让waitThread调用wait方法前先判断状态是否已经改变了没，
 * 如果通知早已发出的话，WaitThread就不再去wait。对上面的代码进行更正：
 * <p>
 * 总结：
 *  在使用线程的等待/通知机制时，一般要配合一个boolean变量值（或者其他能够判断真假的条件），
 *  在notify之前改变该boolean变量的值，让wait返回后能够退出while循环(一般在wait方法外围加
 *  一层while循环，以防止过早的通知)，或在通知被遗漏后，不会被阻塞在wait方法处，这样保证程序的
 *  正确性。
 *
 *
 *
 * @author: mahao
 * @date: 2019/8/13
 */
public class EarlyNotify2 {

    private static String lockObject = "";
    private static boolean isWait = true;//是否允许wait

    public static void main(String[] args) {
        WaitThread waitThread = new WaitThread(lockObject);
        NotifyThread notifyThread = new NotifyThread(lockObject);
        notifyThread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitThread.start();
    }

    static class WaitThread extends Thread {
        private String lock;

        public WaitThread(String lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (isWait) {
                    try {
                        System.out.println(Thread.currentThread().getName() + "  进去代码块");
                        System.out.println(Thread.currentThread().getName() + "  开始wait");
                        lock.wait();
                        System.out.println(Thread.currentThread().getName() + "   结束wait");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class NotifyThread extends Thread {
        private String lock;

        public NotifyThread(String lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "  进去代码块");
                System.out.println(Thread.currentThread().getName() + "  开始notify");
                lock.notify();
                isWait = false;
                System.out.println(Thread.currentThread().getName() + "   结束开始notify");
            }
        }
    }
}

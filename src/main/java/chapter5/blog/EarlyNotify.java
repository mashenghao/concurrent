package chapter5.blog;

/**
 * notify早期通知：
 * <p>
 * A线程wait，B线程notify，当B线程先执行了，A线程还未进入wait状态。
 * 则会在A运行的时候，A陷入wait，一直没被唤醒。
 *
 * @author: mahao
 * @date: 2019/8/13
 */
public class EarlyNotify {

    private static String lockObject = "";

    /*
    示例中开启了两个线程，一个是WaitThread，另一个是NotifyThread。
    NotifyThread会先启动，先调用notify方法。
    然后WaitThread线程才启动，调用wait方法，
    但是由于通知过了，wait方法就无法再获取到相应的通知，
    因此WaitThread会一直在wait方法出阻塞，这种现象就是通知过早的现象。

    针对这种现象，解决方法，一般是添加一个状态标志，
    让waitThread调用wait方法前先判断状态是否已经改变了没，
    如果通知早已发出的话，WaitThread就不再去wait。
     */
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
                System.out.println(Thread.currentThread().getName() + "   结束开始notify");
            }
        }
    }
}

package dpart1;

import chapter3.ThreadUtil;

/**
 * wait方法中的waitSet的描述：
 * <p>
 * 1.wait set 又叫等待池，当某个对象调用了wait()方法后，当前线程就会
 * 暂时停止操作，进入当前锁对象的wait set中，等待唤醒。
 * <p>
 * <p>
 * 2.唤醒方式：
 * 1.调用其他线程调用当前锁对象的notify()
 * 2.调用其他线程调用当前锁对象的notifyAll()
 * 3.其他线程调用了当前线程的interrupt()方法，进行中断线程。
 * <p>
 * 3.wait()方法：
 * 会使当前线程放弃锁，是线程进入阻塞状态，等待其他线程唤醒，近视进入锁对象的
 * wait set中了。
 * 4.notify方法
 * 从wait set拿出线程使用notify(通知)方法时，可从wait set里抓出一个线程
 */
public class WaitSet {

    public static void main(String[] args) {

        //创建多个线程，然后都让他们陷入wait状态，然后一个个唤醒
        //观察线程被唤醒的规则；
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    synchronized (WaitSet.class) {
                        try {
                            System.out.println(ThreadUtil.getName() + " will in wait ");
                            WaitSet.class.wait();
                            System.out.println(ThreadUtil.getName() + " out wait ");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        ThreadUtil.sleep(5000);//等待5s，让线程都陷入wait状态
        System.out.println("will call notify ..");

        /*
        通过输出的结果，可以判断出线程被唤醒的规则不是顺序的，比如FIFO规则，应该是抢占
        或者其他规则的唤醒线程。
         */
        for (int i = 0; i < 10; i++) {
            synchronized (WaitSet.class) {//释放对象的操作，也必须获取到监视器对象
                //获取的监视器和要进行释放锁的对象必须是同一个，否则会出现错误，叫做非法监视器状态异常。
                WaitSet.class.notify();
                ThreadUtil.sleep(1000);
            }

        }
    }

}

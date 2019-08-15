package chapter6;


import chapter3.ThreadUtil;

/**
 * 当一个线程是死线程，一直获得锁，另一个线程一直等待，
 * 设置一种锁，可以设置获得锁的超时时间。
 */
public class MainClass3 {

    public static void main(String[] args) {

        BooleanLock lock = new BooleanLock();

        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    lock.lock(100);
                    System.out.println("t1 ");
                    /*while (true) {
                        //  执行，一直获得锁
                    }*/
                    ThreadUtil.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Lock.TimeOutException e) {
                    e.printStackTrace();
                } finally {
                    lock.unLock();
                }

               /* synchronized (MianClass3.class) {
                    System.out.println("t1 ");
                    while (true) {
                        //  执行，一直获得锁
                    }
                }*/

            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    lock.lock(1000);
                    System.out.println("t2 ");
                    while (true) {
                        //  执行，一直获得锁
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Lock.TimeOutException e) {
                    e.printStackTrace();
                } finally {
                    lock.unLock();
                }


                /*synchronized (MianClass3.class) {//等到t1释放
                    System.out.println("t2 ");
                    while (true) {
                        //  执行，一直获得锁
                    }
                }*/

            }
        };

        t1.start();
        t2.start();
    }
}
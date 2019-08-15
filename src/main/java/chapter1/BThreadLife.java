package chapter1;

/**
 * 2.线程的生命周期
 *
 * new Runnable running blocked dead
 */
public class BThreadLife {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread();
        t.start();
        try {

            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (BThreadLife.class){
            System.out.println("主线程");
        }
    }


     static class MyThread extends Thread{
        @Override
        public void run() {
            synchronized (BThreadLife.class) {
                System.out.println("子线程 开始");

                try {
                    //调用sleep方法，线程进入blocked ，但任然持有锁，主线程必须等待该线程释放锁。
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("子线程  结束");
            }
        }
    }
}

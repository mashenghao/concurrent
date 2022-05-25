package chapter3;

/**
 * 中断：
 * <p>
 * 当对
 *
 * @author: mahao
 * @date: 2019/8/10
 */
public class DInterrupt {

    public static void main(String[] args) {
        //  无法中断线程，
     /*   Thread t = new Thread(){
            @Override
            public void run() {
                while (true){
                    System.out.println(">> " + this.isInterrupted());

                }
            }
        };
        t.start();
        System.out.println(t.isInterrupted());
        t.interrupt();
        System.out.println(t.isInterrupted());
    */


        /*
        如果该线程阻塞的调用wait() ， wait(long) ，或wait(long, int)的方法Object类，
        或者在join() ， join(long) ， join(long, int) ，
         sleep(long) ，或sleep(long, int) ，这个类的方法，
         那么它的中断状态将被清除，并且将收到一个InterruptedException 。
         */
        //  通过sleep wait join 打断线程
        Thread t2 = new Thread() {
            @Override
            public void run() {

                try {
                    while (true) {
                        System.out.println(">> " + this.isInterrupted());
                        //方式一 通过sleep，中断程序
                         // Thread.sleep(1000);  //sleep interrupted


                        //方式二： 通过wait打断信号，我也没看懂
                      /*  synchronized (this) {
                            this.wait(); //让当前线程处于wait状态，知道锁对象调用notify唤醒线程。
                        }
                      */

                        //方式三，通过join打断
                      //  this.join();  //join只能放在里面，join调用的也是wait方法，是让当前线程处于等待状态

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("线程停止.....");
                }
            }
        };

        t2.start();
        Thread main = Thread.currentThread();
        Thread t3 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               // t2.interrupt();//中断线程t2
                main.interrupt();//中断main线程

            }
        };

        t3.start();
        try {
            main.join();  //中断main线程
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(t2.isInterrupted());

    }
}

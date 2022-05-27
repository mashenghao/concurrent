package chapter3_methods;

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
        //  测试1: 线程中一致没有执行 wait sleep await方法等，则一直无法中断线程，
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
                        this.join();  //join只能放在里面，join调用的也是wait方法，是让当前线程处于等待状态,这种会一直卡着的，除非智能由外部中断结束。自己等待自己运行结束。

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

/**
 * 判断Thread类的中断方法：
 */
class Test2 {
    public static void main(String[] args) {

        //测试1： 测试线程的中断位设置与查看线程是否中断了。
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    this.join();
                } catch (InterruptedException e) {
                    System.err.println("t1 线程中断执行,当前中断状态为应该为false，已经抛出了中断异常，会重置状态位:" + this.isInterrupted());
                }
            }
        };
        t1.start();

        ThreadUtil.sleep(1000);

        System.out.println("中断前请求t1中断状态，应该是false： " + t1.isInterrupted());
        t1.interrupt();
        System.out.println("中断后调用t1查看中断状态可能为false也可能为true,主要看t1线程抛出异常的时间在这个输出的前后：" + t1.isInterrupted());


        /*
        测试2: 线程本身轮训校验是否线程被中断了，通过Thread.interrupted()方法，
          如果线程是中断标识，则返回的是true，并且会将标志位设置为false。 一般这种，线程本身就要抛出中断异常了。
          如果线程是未被中断标识，则返回的是false，并且会将标志位设置为false。

          Thread.interrupted()用于线程内部完成一循环任务后，检测下是否要中断停止。


         */
        System.out.println("main线程当前的中断状态应该为false:" + Thread.interrupted());
        Thread.currentThread().interrupt();
        System.out.println("main线程调用中断方法后状态应该为true: this.isInterrupted--: " + Thread.currentThread().isInterrupted() + ",Thread.interrupted--：" + Thread.interrupted());

        System.out.println("调用清理后的，main的状态为false: this.isInterrupted--: " + Thread.currentThread().isInterrupted() + ",Thread.interrupted--: " + Thread.interrupted());
    }
}

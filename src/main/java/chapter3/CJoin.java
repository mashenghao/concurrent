package chapter3;

import sun.misc.ThreadGroupUtils;
import sun.security.krb5.internal.ccache.CCacheInputStream;

/**
 * join方法
 * 让“主线程”等待“子线程”结束之后才能继续运行。
 * Waits for this thread to die
 * 这句话是等待这个线程，知道这个线程结束，可以知道线程的主体还是父线程，是父线程主动陷入等待
 * 状态，让join的线程去执行。
 *
 * @author: mahao
 * @date: 2019/8/10
 */
public class CJoin {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "------->" + i);
                    ThreadUtil.sleep(100);
                }
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 15; i++) {
                    System.out.println(Thread.currentThread().getName() + "------->" + i);
                    ThreadUtil.sleep(100);
                }
            }
        };
        t1.start();
        t2.start();
        t1.join(); //等待线程运行结束，才继续向下执行。

        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "------->" + i);
            ThreadUtil.sleep(100);
        }
        t2.join();
        System.out.println("end ....");
    }


}

class Join2 {

    /**
     * 执行的结构后，主线程main等待子线程t1结束，t1等待子线程tt1结束，
     * 猜想执行是，tt1 执行，t1执行，最后是main线程。
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // >> 子线程
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                // >> 子子线程
                Thread tt1 = new Thread("tt1") {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            System.out.println(Thread.currentThread().getName() + "------->" + i);
                            ThreadUtil.sleep(100);
                        }
                    }
                };
                tt1.start();
                try {
                    tt1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "------->" + i);
                    ThreadUtil.sleep(100);
                }
            }
        };
        t1.start();
        t1.join();
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "------->" + i);
            ThreadUtil.sleep(100);
        }
    }
}

/**
 * 一般来说，阻塞函数，如：Thread.sleep、Thread.join、Object.wait等在检查到线程的中断状态时，会抛出InterruptedException，同时会清除线程的中断状态
 *
 * 对于InterruptedException的处理，可以有两种情况：
 * （1）外层代码可以处理这个异常，直接抛出这个异常即可
 * （2）如果不能抛出这个异常，比如在run()方法内，因为在得到这个异常的同时，线程的中断状态已经被清除了，需要保留线程的中断状态，则需要调用Thread.currentThread().interrupt()
 *
 * 另外，Thread.interrupted()在jdk库的源代码中比较常用，因为它既可以得到上一次线程的中断标志值，又可以同时清除线程的中断标志，一举两得，但同时也有坏处，就是这个函数有清除中断状态的副作用，不容易理解。
 *
 *
 *
 * 从上面的分析可以查出，通过中断机制也可以实现线程的终止，但是这种方式也是基于抛出异常的，所以这种方式也是不安全的，同时我们也可以这样理解就是中断机制就是来结束那些阻塞的线程的，因为阻塞线程回去检查中断标志位，又中断就抛异常来结束线程的运行。
 * ---------------------
 * 版权声明：本文为CSDN博主「尼古拉斯_赵四」的原创文章，遵循CC 4.0 by-sa版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/jiangwei0910410003/article/details/19962603
 */
class Join3 {
    public static void main(String[] args) {

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (this.isInterrupted() == false) {   //如果线程没有被中断就继续运行
                        //阻塞代码：sleep,wait,join等
                        //当其他线程，调用此线程的interrupt()方法时，会给此线程设置一个中断标志
                        //sleep,wait等方法会检测这个标志位，同时会抛出InterruptedException，并清除线程的中断标志    
                        //因此在异常段调用Thread.currentThread().isInterrupted()返回为false     
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //由于阻塞库函数，如：Object.wait,Thread.sleep除了抛出异常外，还会清除线程中断状态，因此可能在这里要保留线程的中断状态    
                    Thread.currentThread().interrupt();//从新设置线程的中断标志    


                }
            }
        };
    }
}

/**
 * 主线程等待主线程结束，会一直陷入等待的状态。
 */
class MainThread {
    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().join();
    }
}
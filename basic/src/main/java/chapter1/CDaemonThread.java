package chapter1;

/**
 * 守护线程的理解：
 *
 *
 */
public class CDaemonThread extends Thread{

    @Override
    public void run() {
        System.out.println("我是守护线程");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Daemon Thread end");
    }

    public static void main(String[] args) {
        Thread thread= new CDaemonThread();
        thread.setDaemon(true);
        thread.start();
        System.out.println("主线程结束");
    }
}

class MyDaemonThread implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("守护线程打印数为---" + i);
        }
    }

    public static void main(String[] args) {
        //主线程打印一句话
        System.out.println("I am main Thread");
        MyDaemonThread myDaemonThread = new MyDaemonThread();
        Thread t = new Thread(myDaemonThread);
        t.setDaemon(true);
        t.start();
        try {
            Thread.sleep(1 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end main Thread ");
    }


}

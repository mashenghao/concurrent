package chapter1;

/**
 * 1.创建并启动线程，让多个操作同时去做:
 * <p>
 * 线程是程序中执行的线程。 Java虚拟机允许应用程序同时执行多个执行线程。
 * <p>
 * 每个线程都有优先权。 具有较高优先级的线程优先于优先级较低的线程执行。 每个线程可能也可能不会被标记为守护程序。
 * 当在某个线程中运行的代码创建一个新的Thread对象时，新线程的优先级最初设置为等于创建线程的优先级，并且当且仅当创建线程是守护进程时才是守护线程。
 * <p>
 * 当Java虚拟机启动时，通常有一个非守护进程线程（通常调用某些指定类的名为main的方法）。 Java虚拟机将继续执行线程，直到发生以下任一情况：
 * <p>
 *  * 已经调用了Runtime类的exit方法，并且安全管理器已经允许进行退出操作。
 *  * 所有不是守护进程线程的线程都已经死亡，无论是从调用返回到run方法还是抛出超出run方法的run 。
 *
 *
 * 创建一个新的执行线程有两种方法。
 * 一个是将一个类声明为Thread的子类。这个子类应该重写run类的方法Thread 。 然后可以分配并启动子类的实例。
 * 另一种方法来创建一个线程是声明实现类Runnable接口。 那个类然后实现了run方法。 然后可以分配类的实例，在创建Thread时作为参数传递，并启动。
 */
public class ATryConcurrency {

    //让读写线程同时执行
    public static void main(String[] args) throws Exception {

        new Thread(() -> {
            try {
                writer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        read();
    }

    public static void read() throws InterruptedException {
        System.out.println("start read ....");
        Thread.sleep(1000L * 50);

        System.out.println("end read ....");
    }

    public static void writer() throws InterruptedException {
        System.out.println("start writer ....");
        Thread.sleep(1000L * 50);

        System.out.println("end writer ....");
    }
}

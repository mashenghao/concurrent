package chapter5_produceConsumer;


import chapter3_methods.ThreadUtil;

import java.util.LinkedList;

/**
 * 综合：
 * <p>
 * 完成一个固定线程数的执行任务：
 *
 * @author: mahao
 * @date: 2019/8/13
 */
public class Test {

    public static void main(String[] args) {
        Service service = new Service(5);

        for (int i = 0; i < 10; i++) {
            final int a = i;
            service.add(() -> {
                ThreadUtil.sleep(3 * 1000);
                System.out.println(ThreadUtil.getName() + "---- 我做工完成了");
            });
        }
        service.execute();

        while (!service.pool.isEmpty()) {

        }
        service.shutdown();
    }

}

class Service {

    volatile LinkedList<Thread> pool = new LinkedList<>();
    int i = 0;
    public final int size;
    Thread executeThread;

    public Service(int size) {
        this.size = size;
    }

    public void add(Runnable task) {
        pool.add(new Thread() {
            @Override
            public void run() {
                task.run();
                synchronized (pool) {
                    i--;
                    pool.notifyAll();
                }
            }
        });
    }

    public void execute() {
        executeThread = new Thread(() -> {
            while (!executeThread.isInterrupted()) {
                synchronized (pool) {
                    while (i >= size || pool.isEmpty()) {
                        try {
                            pool.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            executeThread.interrupt();
                            return;
                        }
                    }

                    i++;
                    System.out.println("程序中存在的线程数是----" + i);
                    Thread t = pool.removeFirst();
                    t.start();
                }
            }
        });
        executeThread.start();

    }

    public void shutdown() {
        executeThread.interrupt();
    }
}
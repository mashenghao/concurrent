package producterConsumer;

import java.util.LinkedList;

/**
 * 生产者
 *
 * @author: mahao
 * @date: 2019/9/3
 */
public class Consumer extends Thread {

    private LinkedList<String> queue;

    public Consumer(LinkedList<String> queue) {

        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                String s = queue.removeFirst();
                System.out.println("Comsumer# consumer the data : " + s);
                queue.notifyAll();
            }
        }
    }
}

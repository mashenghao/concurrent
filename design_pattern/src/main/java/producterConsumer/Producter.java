package producterConsumer;

import chapter3.ThreadUtil;

import java.util.LinkedList;

/**
 * 生产者
 *
 * @author: mahao
 * @date: 2019/9/3
 */
public class Producter extends Thread {

    private LinkedList<String> queue;
    private final int size;
    int i = 0;

    public Producter(LinkedList<String> queue, int size) {
        this.queue = queue;
        this.size = size;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (queue) {
                while (queue.size() > size) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
                queue.addLast(i++ + ThreadUtil.getName());
                queue.notifyAll();
            }
        }
    }
}

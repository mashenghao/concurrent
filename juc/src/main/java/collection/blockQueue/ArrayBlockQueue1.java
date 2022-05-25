package collection.blockQueue;

import atomic.ThreadUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *使用阻塞队列实现生产者与消费模式
 *
 *
 * @author: mahao
 * @date: 2019/10/4
 */
public class ArrayBlockQueue1 {

    private BlockingQueue<Integer> list = new ArrayBlockingQueue<>(5);
    int i;

    public static void main(String[] args) {
        ArrayBlockQueue1 pc = new ArrayBlockQueue1();
        ArrayBlockQueue1.Producer p = pc.new Producer();
        new Thread(p).start();
        new Thread(p).start();
        ArrayBlockQueue1.Consumer c = pc.new Consumer();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();
    }

    class Producer implements Runnable {

        public void run() {

            while (true) {
                try {
                    System.out.println("P-> " + ThreadUtil.getName() + "  " + i);
                    list.put(i++);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ThreadUtil.randSleep(10, 1000);
            }

        }
    }

    class Consumer implements Runnable {

        public void run() {
            while (true) {
                try {
                    Integer take = list.take();
                    System.out.println("C-> " + ThreadUtil.getName() + "  " + take);
                    i--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ThreadUtil.randSleep(5, 1000);
            }

        }
    }
}

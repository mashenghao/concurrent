package collection.blockQueue;

import atomic.ThreadUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列的使用,改写生产者和消费者
 *
 * @author: mahao
 * @date: 2019/10/4
 */
public class ProducerComsumer {

    private List<Integer> list = new LinkedList<>();
    Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private final int Max_size = 5;
    int i = 0;

    public static void main(String[] args) {
        ProducerComsumer pc = new ProducerComsumer();
        Producer p = pc.new Producer();
        new Thread(p).start();
        new Thread(p).start();
        Consumer c = pc.new Consumer();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();
    }

    class Producer implements Runnable {

        public void run() {

            while (true) {
                try {
                    lock.lock();
                    while (list.size() == Max_size) {
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("P-> " + ThreadUtil.getName()+"  " + i);
                    list.add(i++);
                    notEmpty.signal();
                } finally {
                    lock.unlock();
                }
                ThreadUtil.randSleep(10, 1000);
            }

        }
    }

    class Consumer implements Runnable {

        public void run() {
            while (true) {
                try {
                    lock.lock();
                    while (list.size() == 0) {
                        try {
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer integer = list.remove(0);
                    System.out.println("C-> " + ThreadUtil.getName()+"  " + integer);
                    notFull.signal();
                } finally {
                    lock.unlock();
                }
                ThreadUtil.randSleep(5, 1000);
            }

        }
    }
}

package lock.condition;

import atomic.ThreadUtil;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition条件，使用两个条件阻塞队列，存
 * 消费和生产线程，一个condition存生产线程，另一个存储
 * 消费线程。
 *
 * @author: mahao
 * @date: 2019/9/26
 */
public class Condition2 {

    private final static Lock lock = new ReentrantLock();
    private final static int LIMIT = 10;
    private static Condition build_condition = lock.newCondition();
    private static Condition consumer_condition = lock.newCondition();

    static LinkedList<Integer> list = new LinkedList<>();


    private void build() {
        try {
            lock.lock();
            while (list.size() > LIMIT) {
                build_condition.await();
            }
            ThreadUtil.sleep(2000);
            list.addLast(1);
            System.out.println("P--> " + ThreadUtil.getName() + "---" + list.size());
            consumer_condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void consumer() {
        try {
            lock.lock();
            while (list.size() <= 0) {
                consumer_condition.await();
            }
            list.removeFirst();
            ThreadUtil.sleep(2000);
            System.out.println("C--> " + ThreadUtil.getName() + "---" + list.size());
            build_condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        Condition2 condition1 = new Condition2();
        Runnable p = () -> {
            for (; ; )
                condition1.build();
        };
        Runnable c = () -> {
            for (; ; )
                condition1.consumer();
        };

        new Thread(p).start();
        new Thread(p).start();
        new Thread(c).start();
        new Thread(c).start();

    }

}

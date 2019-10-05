package lock.condition;

import atomic.ThreadUtil;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition条件，使用单个条件阻塞队列，存
 * 消费和生产线程
 *
 * @author: mahao
 * @date: 2019/9/26
 */
public class Condition1 {

    private final static Lock lock = new ReentrantLock( );
    private final static int LIMIT = 10;
    private static Condition condition = lock.newCondition();

    static LinkedList<Integer> list = new LinkedList<>();


    private void build() {
        try {
            lock.lock();
            while (list.size() > LIMIT) {
                condition.await();
            }
            ThreadUtil.sleep(2000);
            list.addLast(1);
            System.out.println("P--> " + ThreadUtil.getName() + "---" + list.size());
            condition.signal();
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
                condition.await();
            }
            list.removeFirst();
            ThreadUtil.sleep(2000);
            System.out.println("C--> " + ThreadUtil.getName() + "---" + list.size());
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        Condition1 condition1 = new Condition1();
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

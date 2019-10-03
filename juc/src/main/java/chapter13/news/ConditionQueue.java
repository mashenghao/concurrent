package chapter13.news;

import chapter1.ThreadUtil;
import chapter12.src.ReentrantLock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;

/**
 * 使用condition实现有界队列
 *
 * @author: mahao
 * @date: 2019/10/1
 */
public class ConditionQueue<T> {
    private ReentrantLock lock = new ReentrantLock();
    private Condition pcondition = lock.newCondition();
    private Condition ccondition = lock.newCondition();

    private Object[] objs;
    int h;
    int t;
    int n;

    public ConditionQueue(int size) {
        n = size;
        objs = new Object[size];
    }


    public void push(T e) {
        try {
            lock.lock();
            while ((t + 1) % n == h) {
                try {
                    pcondition.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            objs[t] = e;
            t = (t + 1) % n;
            ccondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public T pop() {
        try {
            lock.lock();
            while (h == t) {
                try {
                    ccondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T e = (T) objs[h];
            h = (h + 1) % n;
            pcondition.signal();
            return e;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return (t - h + n) % n;
    }


    public static void main(String[] args) {
        ConditionQueue<Integer> conditionQueue = new ConditionQueue<>(10);
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Runnable p = () -> {
            while (true) {
                int i = atomicInteger.getAndIncrement();
                conditionQueue.push(i);
                System.out.println("p---> " + i);
                ThreadUtil.sleep(1000);
            }

        };
        Runnable c = () -> {
            while (true) {
                int s = conditionQueue.pop();
                System.out.println("c---> " + s);
                ThreadUtil.sleep(1000);
            }

        };

        new Thread(p).start();
        new Thread(p).start();
        new Thread(p).start();
        new Thread(p).start();
        new Thread(c).start();
        new Thread(c).start();
    }


}

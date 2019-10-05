package lock.chapter11;

import atomic.ThreadUtil;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: mahao
 * @date: 2019/9/23
 */
public class PC2 {


    public static void main(String[] args) {
        SynList<String> list = new SynList<>();
        Producter p = new Producter(list);
        Consumer c = new Consumer(list);

        new Thread(p).start();
        new Thread(p).start();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();

    }

    static class Producter implements Runnable {
        SynList<String> list;

        public Producter(SynList<String> list) {
            this.list = list;
        }

        public void run() {
            while (true) {
                list.put("a");
                System.out.println("Producter : " + "s" + "size: " + list.getCount());
                ThreadUtil.sleep(1000);
            }
        }
    }

    static class Consumer implements Runnable {
        SynList<String> list;

        public Consumer(SynList<String> list) {
            this.list = list;
        }

        public void run() {
            while (true) {
                String s = list.pop();
                System.out.println("Consumer : " + "s" + "size: " + list.getCount());
                ThreadUtil.sleep(2000);
            }
        }
    }

    static class SynList<T> {

        private final Lock lock = new ReentrantLock(true);

        private final int MAX_SIZE = 10;
        private LinkedList<T> list = new LinkedList<>();

        public void put(T t) {
            for (; ; ) {
                try {
                    lock.lock();
                    if (list.size() != MAX_SIZE) {
                        list.addLast(t);
                        return;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        public T pop() {
            for (; ; ) {
                try {
                    lock.lock();
                    if (list.size() != 0) {
                        return list.removeFirst();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        public int getCount() {
            try {
                lock.lock();
                return list.size();
            } finally {
                lock.unlock();
            }

        }

    }
}

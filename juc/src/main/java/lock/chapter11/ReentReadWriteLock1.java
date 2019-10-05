package lock.chapter11;

import atomic.ThreadUtil;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: mahao
 * @date: 2019/9/23
 */
public class ReentReadWriteLock1 {

    static final Integer[] arr = new Integer[10];
    static int i = 0;

    public static void main(String[] args) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
        new Thread(new ReadThread(lock.readLock(), arr)).start();
        new Thread(new WriteThread(lock.writeLock(), arr)).start();

    }

    static class ReadThread implements Runnable {

        Lock lock;
        Integer[] arr;

        public ReadThread(Lock lock, Integer[] arr) {
            this.lock = lock;
            this.arr = arr;
        }

        public void run() {
            while (true) {
                try {
                    lock.lock();
                    System.out.println("readLock thread ........ ");
                    for (int i = 0; i < arr.length; i++) {
                        System.out.print(arr[i] + " ");
                    }
                    System.out.println();
                    ThreadUtil.sleep(1000);
                } finally {
                    lock.unlock();
                }

            }

        }
    }

    static class WriteThread implements Runnable {

        Lock lock;
        Integer[] arr;

        public WriteThread(Lock lock, Integer[] arr) {
            this.lock = lock;
            this.arr = arr;
        }

        public void run() {
            while (true) {
                try {
                    lock.lock();
                    i++;
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = ReentReadWriteLock1.i;
                    }
                    System.out.println("writeLock thread .........." +Arrays.asList(arr));
                    ThreadUtil.sleep(2000);
                } finally {
                    lock.unlock();
                }

            }


        }
    }
}

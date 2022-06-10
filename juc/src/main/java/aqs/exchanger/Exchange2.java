package aqs.exchanger;

import atomic.ThreadUtil;

import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 案例： 两个线程交替更给Integer的值
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class Exchange2 {

    static final Exchanger<Integer> exchanger = new Exchanger<Integer>();


    static class ThreadA implements Runnable {
        public void run() {
            AtomicReference<Integer> atomicReference = new AtomicReference<Integer>(1);

            while (true) {
                try {
                    Integer exchange = exchanger.exchange(atomicReference.get());
                    atomicReference.getAndSet(exchange);
                    System.out.println("Thread A : " + atomicReference.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ThreadB implements Runnable {

        public void run() {
            AtomicReference<Integer> atomicReference = new AtomicReference<Integer>(0);
            while (true) {
                try {
                    ThreadUtil.sleep(3000);
                    Integer exchange = exchanger.exchange(atomicReference.get());
                    atomicReference.getAndSet(exchange);
                    System.out.println("Thread B : " + atomicReference.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new ThreadA()).start();
        new Thread(new ThreadB()).start();

    }
}

package chapter13.news;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 顺序调用三个方法，通过condition
 *
 * @author: mahao
 * @date: 2019/10/1
 */
public class Condition1 {


    volatile int state = 1;

    public synchronized void m1() {
        while (state != 1) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("a");
        state = 2;
        this.notifyAll();
    }

    public synchronized void m2() {
        while (state != 2) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("b");
        state = 3;
        this.notifyAll();
    }

    public synchronized void m3() {
        while (state != 3) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("c");
        state = 1;
        this.notifyAll();
    }


    public static void main(String[] args) {
        final Condition1 condition1 = new Condition1();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    condition1.m1();
                    condition1.M1();
                }

            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    condition1.m2();
                    condition1.M2();
                }

            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    condition1.m3();
                    condition1.M3();
                }

            }
        }.start();
    }

    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();
    volatile int state2 = 1;

    public void M1() {
        lock.lock();
        while (state2 != 1) {
            try {
                c1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("1");
        state2 = 2;
        c2.signal();
        lock.unlock();
    }

    public void M2() {
        lock.lock();
        while (state2 != 2) {
            try {
                c2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("2");
        state2 = 3;
        c3.signal();
        lock.unlock();
    }

    public void M3() {
        lock.lock();
        while (state2 != 3) {
            try {
                c3.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("3");
        state2 = 1;
        c1.signal();
        lock.unlock();
    }
}

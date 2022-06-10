package aqs.semaphore;

import atomic.ThreadUtil;

import java.util.concurrent.Semaphore;

/**
 * Semaphore讲解：
 * 利用semaphore构造一个显示同步锁
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class Semaphore1 {

    public static void main(String[] args) {
        SemaphoreLock lock = new SemaphoreLock();

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    lock.lock();
                    System.out.println(ThreadUtil.getName() + "is running ....");
                    ThreadUtil.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }).start();
        }
    }
}

class SemaphoreLock {

    private final Semaphore semaphore = new Semaphore(1);

    public void lock() throws InterruptedException {
        semaphore.acquire();
    }

    public void unlock() {
        semaphore.release();
    }
}
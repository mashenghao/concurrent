package chapter6;

import sun.misc.Unsafe;

import javax.xml.ws.soap.Addressing;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 比价实现几种并发控制方式，比较处理时间。
 * 案例：
 * <p>
 * 线程并发操作一个计数器，计数器内部实现并发控制，计数器从15个线程，每个线程
 * 数量加到1万，查看需要的时间。
 * <p>
 * 1. StupiedCounter：
 *   result: 7233992
 *  time: 1820
 * 2. SynchronizedCounter：
 *  result: 10000000
 *  time: 2427
 * 3.AtomicCounter
 *  result: 10000000
 *  time: 2151
 * 4.UnsafeCounter
 *  result: 10000000
 *  time: 2453
 * 5.LockCounter
 *  result: 10000000
 *  time: 1488
 *
 * @author: mahao
 * @date: 2019/9/22
 */
public class CounterTest {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService service = Executors.newFixedThreadPool(1000);

        Counter counter = new UnsafeCounter();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            service.submit(new CounterRunnable(counter, 10000));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();
        System.out.println("result: " + counter.get());
        System.out.println("time: " + (end - start));

    }

    static interface Counter {
        int addAndGet();

        int get();
    }

    /**
     * 没有任何并发控制，只适用于单线程。
     */
    static class StupiedCounter implements Counter {
        private int count;

        public int addAndGet() {
            return ++count;
        }

        public int get() {
            return count;
        }
    }

    /**
     * 使用同步锁实现
     */
    static class SynchronizedCounter implements Counter {

        private int count;

        public synchronized int addAndGet() {
            return ++count;
        }

        public int get() {
            return count;
        }
    }

    /**
     * 3.使用AtomicInteger原子类实现
     */
    static class AtomicCounter implements Counter {

        private AtomicInteger atomic = new AtomicInteger(0);


        @Override
        public int addAndGet() {
            return atomic.getAndIncrement();
        }

        @Override
        public int get() {
            return atomic.get();
        }
    }

    /**
     * 4.使用Unsafe并发包，自定义实现并发控制
     */
    static class UnsafeCounter implements Counter {

        private volatile int count;
        private Unsafe unsafe = null;
        private final long valueOffset;

        public UnsafeCounter() {
            unsafe = getUnsafe();
            try {
                valueOffset = unsafe.objectFieldOffset
                        (UnsafeCounter.class.getDeclaredField("count"));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new RuntimeException("error  ");
            }
        }

        private static Unsafe getUnsafe() {
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                return (Unsafe) field.get(null);
            } catch (Exception e) {
                throw new RuntimeException("error");
            }
        }


        @Override
        public int addAndGet() {
            for (; ; ) {
                int current = count;
                int next = count + 1;
                if (unsafe.compareAndSwapInt(this, valueOffset, current, next))
                    return current;
            }

        }

        @Override
        public int get() {
            return count;
        }
    }

    /**
     * 使用显示锁实现
     */
    static class LockCounter implements Counter {
        private final Lock lock = new ReentrantLock();

        private int count;

        @Override
        public int addAndGet() {
            try {
                lock.lock();
                return count++;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int get() {
            return count;
        }
    }

    static class CounterRunnable implements Runnable {

        private Counter counter;
        private int n;

        public CounterRunnable(Counter counter, int n) {
            this.counter = counter;
            this.n = n;
        }

        @Override
        public void run() {
            for (int i = 0; i < n; i++) {
                counter.addAndGet();
            }
        }


    }


}

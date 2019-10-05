package future.news;

import future.Callable;
import future.Future;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * RunnableFuture将Future和FutureService整合起来
 *
 * @author: mahao
 * @date: 2019/10/3
 */
public class RunnableFuture<T> implements Future<T>, Runnable {

    private Callable<T> callable;
    private volatile boolean done = false;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    private T v;

    Consumer<T> consumer;

    public RunnableFuture(Callable<T> callable) {
        this.callable = callable;
    }

    //重载，回调函数
    public RunnableFuture(Consumer<T> consumer, Callable<T> callable) {
        this.callable = callable;
        this.consumer = consumer;
    }

    public T get() throws InterruptedException {
        try {
            lock.lockInterruptibly();
            while (!done) {
                condition.await();
            }
            return v;
        } finally {
            lock.unlock();
        }

    }


    public void run() {

        try {
            lock.lock();
            T result = callable.call();
            done = true;
            v = result;
            if (consumer != null)
                consumer.accept(result);
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            if (!done) {
                throw new RuntimeException("error ");
            }

        }
    }
}

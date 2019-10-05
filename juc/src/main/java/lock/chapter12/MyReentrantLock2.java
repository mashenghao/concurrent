package lock.chapter12;


import lock.src.AbstractQueuedSynchronizer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 用AQS实现可重入锁
 *
 * @author: mahao
 * @date: 2019/9/28
 */
public class MyReentrantLock2 implements Lock {

    private final Sync sync = new Sync();

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            int state = getState();
            if (state == 0) {
                if (!hasQueuedPredecessors() && compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            } else if (Thread.currentThread() == getExclusiveOwnerThread()) {
                setState(state + arg);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            int state = getState() - arg;
            boolean free = false;
            if (state == 0) {
                setExclusiveOwnerThread(null);
                free = true;
            }
            setState(state);
            return free;
        }

        @Override
        protected boolean isHeldExclusively() {
            return true;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}

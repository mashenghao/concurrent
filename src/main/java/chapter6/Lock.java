package chapter6;

import java.util.Collection;

/**
 * 自定义实现一个显示锁
 *
 * @Date: 2019/8/13 22:07
 * @Author: mahao
 */
public interface Lock {

    //获取锁
    void lock() throws InterruptedException;

    //获取锁，超时失败
    void lock(long millos) throws InterruptedException, TimeOutException;

    //释放锁
    void unLock();

    Collection<Thread> getBlockThread();

    int getBlockSize();


    class TimeOutException extends Exception {

        public TimeOutException(String msg) {
            super(msg);
        }
    }
}

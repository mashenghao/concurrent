package dpart9;

import chapter3.ThreadUtil;

/**
 * ThreadLocal简易测试使用说明
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class ThreadLocalSimpleTest {

    /**
     * 两个线程共享一个ThreadLocal实例，实例变量的数据不会有并发问题。
     * 原因是ThreadLocal是获取当前线程中Thread的ThreadLocalMap属性，这个
     * 是每个线程各有的一个变量。
     *
     * @param args
     */
    public static void main(String[] args) {

        final ThreadLocal<String> threadLocal = new ThreadLocal();

        new Thread() {
            @Override
            public void run() {
                threadLocal.set("Thread-0");
                ThreadUtil.sleep(3000);
                System.out.println(ThreadUtil.getName() + " threadlocal: " + threadLocal.get());
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                threadLocal.set("Thread-1");
                ThreadUtil.sleep(1000);
                System.out.println(ThreadUtil.getName() + " threadlocal: " + threadLocal.get());
            }
        }.start();
    }
}

package chapter8.src.ExecutorService;

import chapter3.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 线程池中的任务出现异常的处理，
 * 比如，10个任务更改数据库，有的更改失败了，线程池的执行方法是不会抛出异常的，
 * 怎么让外部调用线程去察觉到失败，然后去处理这个错误。
 * <p>
 * 方式1： 通过给执行线程设置异常处理的方法回调。
 * 方法2：自定义run方法，使用模版模式设计方法。
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class ExecutorService2 {

    public static void main(String[] args) {

        final MyExceptionHandler exceptionHandler = new MyExceptionHandler();
        //通过给执行线程设置异常处理的方法回调。
        ExecutorService pool1 = Executors.newCachedThreadPool(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setUncaughtExceptionHandler(exceptionHandler);
                return t;
            }
        });

        for (int i = 0; i < 5; i++) {
            final int num = i;
            pool1.execute(() -> {
                if (num == 4) {
                    System.err.print("error");
                    throw new NullPointerException();
                } else {
                    System.out.println("正常执行: " + num);
                }
            });
        }


        //解决方式2
      //  method2();
    }

    static class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(ThreadUtil.getName() + "  " + t.getName() + ": " + e.toString());
        }
    }
    /**
     * +====================================================
     * 方式2，使用模版方法设计模式，设计任务类，对于任务类各部分都有实现。
     */
    public static void method2() {
        ExecutorService pool2 = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            final int num = i;
            pool2.execute(new MyTask() { //通过执行自定义的模版方法

                @Override
                protected void execute() {
                    if (num == 4) {
                        System.err.print("error");
                        throw new NullPointerException();
                    } else {
                        System.out.println("正常执行: " + num);
                    }
                }

                @Override
                protected void exception(Thread currentThread, Exception e) {
                    System.out.println(currentThread + "  " + e.toString());
                }

                @Override
                protected void after() {

                }

            });
        }
    }

    static abstract class MyTask implements Runnable {

        public void run() {
            try {
                execute();
            } catch (Exception e) {
                exception(Thread.currentThread(), e);
            } finally {
                after();
            }
        }

        protected abstract void after();

        protected abstract void exception(Thread currentThread, Exception e);

        protected abstract void execute();
    }


}

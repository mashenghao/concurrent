package future;

import atomic.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Future 常用api
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class Future2 {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<String> future = executor.submit(() -> {
            System.out.println("callable start ... ");
            ThreadUtil.sleep(5000);
            System.out.println("callable end ... ");
            return "aa";
        });

        //future.get(1, TimeUnit.SECONDS);



        /**
         * 如果调用了中断，则会发生抛出中断异常，抛出的异常时主线程等待获取数据造成的
         * 异常，而不是执行线程中断。所以，当调用了中断方法，并没有中断执行线程，只是中断
         * 了get方法中的wait。
         */
        Thread callThread = Thread.currentThread();
        callThread.interrupt();



        System.out.println("before isDone: " + future.isDone());
        String s = future.get();

        System.out.println("after isDone: " + future.isDone());
        System.out.println(s);

        /**
         * 看源码理解
         */
        boolean f = future.cancel(true);
        System.out.println("是否取消成功： "+ f);
    }

}

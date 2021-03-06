package chapter8.src.FeatureAndCallable;

import chapter3.ThreadUtil;

import java.util.concurrent.*;

/**
 * @author: mahao
 * @date: 2019/10/5
 */
public class Future1 {

    public static void main(String[] args) throws InterruptedException, Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<String> future = executor.submit(() -> {
            ThreadUtil.sleep(5000);
            return "aa";
        });
        System.out.println(future.get());

        FutureTask<String> future1 = new FutureTask<>(() -> {
            ThreadUtil.sleep(5000);
            return "bb";
        });
        new Thread(future1).start();
        System.out.println(future1.get());
    }
}

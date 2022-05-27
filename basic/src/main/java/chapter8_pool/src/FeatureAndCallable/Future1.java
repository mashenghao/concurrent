package chapter8_pool.src.FeatureAndCallable;

import chapter3_methods.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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

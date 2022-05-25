package future;

import atomic.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 通过线程池执行future中的方法,原理相似，线程池将callable封装成了FutureTask类，然后去线程池
 * 中找工作线程去执行，原理还是和futureTask一致。
 *
 * @author: mahao
 * @date: 2019/10/5
 */
public class Future1 {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<String> future = executor.submit(() -> {
            ThreadUtil.sleep(3000);
            int a = 1 / 0;
            return "123";
        });

        ThreadUtil.sleep(1000);
        System.out.println("do other things");

        String result = future.get();
        System.out.println(result);
    }
}

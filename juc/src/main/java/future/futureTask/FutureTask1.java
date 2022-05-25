package future.futureTask;

import atomic.ThreadUtil;
import future.src.FutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * FutureTask实现了Future接口和Runnable接口，具有执行和获取数据的功能。
 *
 * @author: mahao
 * @date: 2019/10/3
 */
public class FutureTask1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                ThreadUtil.sleep(3000);
                int a = 1 / 0;
                return "123";
            }
        });

        new Thread(futureTask).start();

        ThreadUtil.sleep(1000);
        System.out.println("do other things");

        String result = futureTask.get();
        System.out.println(result);


    }
}

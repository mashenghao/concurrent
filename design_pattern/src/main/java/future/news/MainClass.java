package future.news;


import chapter3_methods.ThreadUtil;
import future.Callable;

/**
 * @author: mahao
 * @date: 2019/10/3
 */
public class MainClass {

    public static void main(String[] args) throws InterruptedException {
        RunnableFuture<String> future = new RunnableFuture<String>(new Callable<String>() {
            @Override
            public String call() {
                ThreadUtil.sleep(3000);
                return "123s";
            }
        });

        new Thread(future).start();

        ThreadUtil.sleep(1000);
        System.out.println("do something");

        String s = future.get();
        System.out.println(s);
    }
}

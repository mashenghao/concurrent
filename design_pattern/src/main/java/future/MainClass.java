package future;

import chapter3_methods.ThreadUtil;

import java.util.function.Consumer;

/**
 * 主要类总结：
 * FutureTask<T>: 需要异步执行的任务，T是任务执行玩不后的返回值，通过方法get获取到数值。
 * FutureService：用来执行异步任务的主类，开辟线程执行任务，执行结束后，设置结果。
 * Future<T>: 任务执行凭证，FutureService提交任务后，返回的结果，用这个对象，去获取返回数值，定义的
 * 是一个接口，具体执行获取通过子类实现，接口暴露T get(),获取结果。
 * AsynFuture<T>: 具体的执行凭证，存取用户提交的任务的结果，判断任务是否执行完毕等任务。
 *
 * @author: mahao
 * @date: 2019/8/28
 */
public class MainClass {

    public static void main(String[] args) throws InterruptedException {
        FutureService futureService = new FutureService();

        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {

                System.out.println(s);
            }
        };

        //通过方法回调获取返回值
        Future<String> future = futureService.submit(() -> {
            //执行异步任务
            ThreadUtil.sleep(10_000);
            return "this is result";
        }, consumer);

        System.out.println("main thread begin");
        ThreadUtil.sleep(3_000);
        System.out.println("main thread end");

        String result2 = future.get();
        System.out.println("阻塞等待异步任务执行完毕--Asyn task result :  " + result2);
    }
}

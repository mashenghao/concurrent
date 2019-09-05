package dpart7;

import java.util.function.Consumer;

/**
 * 执行异步任务，并将执行任务结束后，将结果通知到凭证中
 *
 * @author: mahao
 * @date: 2019/8/28
 */
public class FutureService {

    /**
     * @param task 异步任务
     * @param <T>  任务执行返回值
     * @return 结果凭证
     */
    public <T> Future<T> submit(FutureTask<T> task) {
        //创建一个凭据，作为返回
        final AsynFuture<T> asynFuture = new AsynFuture<>();
        new Thread() {
            @Override
            public void run() {
                T result = task.call();
                //执行完毕后，设置结果
                asynFuture.setResult(result);
            }
        }.start();

        return asynFuture;
    }

    /**
     * 当任务执行完毕后，调用回调函数，执行回调函数的方法
     *
     * @param task
     * @param consumer
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(FutureTask<T> task, Consumer<T> consumer) {
        //创建一个凭据，作为返回
        final AsynFuture<T> asynFuture = new AsynFuture<>();
        new Thread() {
            @Override
            public void run() {
                T result = task.call();
                consumer.accept(result);
                //执行完毕后，设置结果
                asynFuture.setResult(result);
            }
        }.start();

        return asynFuture;
    }
}

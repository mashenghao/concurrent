package dpart7;

/**
 * 获取数据的凭证，通过get方法，获取到提交的任务的结果
 *
 * @Date: 2019/8/28 22:46
 * @Author: mahao
 */
public interface Future<T> {


    /**
     * 返回提交的异步任务的返回值
     *
     * @return 返回值
     * @throws InterruptedException 异步任务执行中，被中断，则会抛出异常
     */
    T get() throws InterruptedException;
}

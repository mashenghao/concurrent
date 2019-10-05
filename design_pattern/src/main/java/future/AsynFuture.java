package future;

/**
 * 具体的任务凭证,作为提交的异步的返回值（Future），
 * 通过get获取到返回结果。
 *
 * @author: mahao
 * @date: 2019/8/28
 */
public class AsynFuture<T> implements Future<T> {

    //变量标识任务是否已经执行完毕
    private volatile boolean done = false;


    private T result;

    //当异步执行线程执行完毕后，会调用方法，将结果传到这里

    public synchronized void setResult(T result) {
        this.result = result;
        done = true;
        notifyAll();//通知get方法，结果返回，唤醒执行
    }

    @Override
    public synchronized T get() throws InterruptedException {
        while (!done) {
            wait();
        }
        return result;
    }
}

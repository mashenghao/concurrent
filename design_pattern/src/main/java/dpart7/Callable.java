package dpart7;

/**
 * 异步任务的接口，单独抽离出来，将执行与任务分开，解耦操作。
 *
 * @author: mahao
 * @date: 2019/8/28
 */
public interface Callable<T> {

    /**
     * 执行的任务方法
     *
     * @return
     */
    T call();
}

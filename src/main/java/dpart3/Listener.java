package dpart3;

/**
 * @program: concurrent
 * @Date: 2019/8/26 19:55
 * @Author: mahao
 * @Description: 监听器
 */
public interface Listener {
    /**
     * 事件被触发调用的方法
     *
     * @param e
     */
    void onEvent(ObservableRunnable.RunnableEvent e);
}

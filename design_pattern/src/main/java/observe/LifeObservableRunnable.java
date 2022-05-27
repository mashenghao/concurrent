package observe;

import chapter3_methods.ThreadUtil;

/**
 * 具体的被监听对象
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class LifeObservableRunnable extends ObservableRunnable {

    public LifeObservableRunnable(Listener listener) {
        super(listener);
    }

    @Override
    public void run() {
        try {
            notifyChange(new RunnableEvent(RunnableStatue.RUNNING, Thread.currentThread(), null));
            System.out.println("thread is running" + ThreadUtil.getName());
            ThreadUtil.sleep(1000);
            notifyChange(new RunnableEvent(RunnableStatue.DONE, Thread.currentThread(), null));

        } catch (Exception e) {
            notifyChange(new RunnableEvent(RunnableStatue.ERROR, Thread.currentThread(), null));
        }

    }
}

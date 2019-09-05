package dpart3;

import chapter3.ThreadUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 视频源思路
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class LifeCycleObservable implements Listener {

    //去并发查询ids
    public void concurrentQuery(final List<String> ids) {
        //创建一个内部类的实现ObservableRunnable

        ObservableRunnable task = new ObservableRunnable(this) {
            int i = 0;

            @Override
            public void run() {
                try {
                    notifyChange(new RunnableEvent(RunnableStatue.RUNNING, Thread.currentThread(), null));
                    System.out.println("thread is running id " + ids.get(i++));
                    ThreadUtil.sleep(1000);
                    int a = 1 / 0;
                    notifyChange(new RunnableEvent(RunnableStatue.DONE, Thread.currentThread(), null));

                } catch (Exception e) {
                    notifyChange(new RunnableEvent(RunnableStatue.ERROR, Thread.currentThread(), e));
                }
            }
        };

        for (String id : ids) {
            new Thread(task).start();
        }
    }

    @Override
    public synchronized void onEvent(ObservableRunnable.RunnableEvent e) {
        System.out.println("the listener fetch the change , thread" + ThreadUtil.getName());
        System.out.println(e.toString());
    }
}

class Client {

    public static void main(String[] args) {
        LifeCycleObservable observable = new LifeCycleObservable();
        observable.concurrentQuery(Arrays.asList("1", "2", "3"));
    }
}
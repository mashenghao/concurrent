package dpart3;

import chapter3.ThreadUtil;

/**
 * 具体的监听类
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class LifeListener implements Listener {

    @Override
    public synchronized void onEvent(ObservableRunnable.RunnableEvent e) {
        System.out.println("the listener fetch the change , thread" + ThreadUtil.getName());
        System.out.println(e.toString());
    }
}

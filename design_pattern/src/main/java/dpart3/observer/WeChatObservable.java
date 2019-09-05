package dpart3.observer;


/**
 * 被监听者
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class WeChatObservable extends Observable {


    @Override
    public void doChange() {
        System.out.println("send message");
        notifyAllChange();
    }


    @Override
    public void notifyAllChange() {
        for (Observer o : observers) {
            o.handler(this, null);
        }
    }


}

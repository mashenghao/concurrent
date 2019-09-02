package dpart3.observer;

/**
 * 观察者模式
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class MainClass {

    public static void main(String[] args) {
        WeChatObservable weChatObservable = new WeChatObservable();
        Observer u1 = (Observable observable, Object arg) -> {
            System.out.println("用户1收到通知");
        };
        Observer u2 = (Observable observable, Object arg) -> {
            System.out.println("用户2收到通知");
        };
        weChatObservable.addObserver(u1);
        weChatObservable.addObserver(u2);
        weChatObservable.doChange();
    }
}

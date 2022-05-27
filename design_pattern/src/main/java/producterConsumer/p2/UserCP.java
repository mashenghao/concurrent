package producterConsumer.p2;

import chapter3_methods.ThreadUtil;

/**
 * @author: mahao
 * @date: 2019/9/3
 */
public class UserCP {

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(5);
        Consumer c1 = new Consumer(queue, "c1");
        Consumer c2 = new Consumer(queue, "c2");
        Consumer c3 = new Consumer(queue, "c3");
        Producter p1 = new Producter(queue, "p1");
        Producter p2 = new Producter(queue, "p2");
        Producter p3 = new Producter(queue, "p3");
        c1.start();
        c2.start();
        c3.start();
        p1.start();
        p2.start();
        p3.start();

        ThreadUtil.sleep(10_000);
        c1.shutdown();
        c2.shutdown();
        c3.shutdown();
        p1.shutdown();
        p2.shutdown();
        p3.shutdown();
    }
}

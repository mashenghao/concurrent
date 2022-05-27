package producterConsumer.p2;

import chapter3_methods.ThreadUtil;

/**
 * @author: mahao
 * @date: 2019/9/3
 */
public class Consumer extends Thread {

    private MessageQueue queue;
    private volatile boolean end = false;

    public Consumer(MessageQueue queue, String name) {
        super(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!end) {
            try {
                int data = queue.offer();
                System.out.println("Consumer.run # " + ThreadUtil.getName() + " consum the data is ：" + data);
                ThreadUtil.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        end = true;
        this.interrupt();
    }
}

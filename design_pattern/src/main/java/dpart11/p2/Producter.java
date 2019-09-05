package dpart11.p2;

import chapter3.ThreadUtil;

/**
 * @author: mahao
 * @date: 2019/9/3
 */
public class Producter extends Thread {

    private final MessageQueue queue;
    private volatile boolean end = false;
    private static int i = 0;

    public Producter(MessageQueue queue, String name) {
        super(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!end) {
            try {
                queue.put(++i);
                System.out.println("Producter.run # " + ThreadUtil.getName() + " product the data: " + i);
                //ThreadUtil.sleep(1000);
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

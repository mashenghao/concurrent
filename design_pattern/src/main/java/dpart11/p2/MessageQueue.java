package dpart11.p2;

import java.util.LinkedList;
import java.util.Map;

/**
 * 也是生产者消费模式，只是将逻辑封装进queue中，类似Guarded Suspension（保护悬挂），
 *
 * @author: mahao
 * @date: 2019/9/3
 */
public class MessageQueue {

    private final int MAXSIZE;
    private LinkedList<Integer> queue = new LinkedList<>();


    public MessageQueue(int MAXSIZE) {
        this.MAXSIZE = MAXSIZE;
    }

    public void put(Integer data) throws InterruptedException {
        synchronized (queue) {
            while (queue.size() > MAXSIZE) {
                queue.wait();
            }
            queue.addLast(data);
            queue.notifyAll();
        }
    }

    public Integer offer() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }
            Integer data = queue.removeFirst();
            queue.notifyAll();
            return data;
        }

    }
}

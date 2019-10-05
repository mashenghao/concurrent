package requestQueue;

import java.util.LinkedList;

/**
 * 请求队列
 *
 * @author: mahao
 * @date: 2019/9/1
 */
public class RequestQueue {

    //请求队列
    private final LinkedList<Request> queue = new LinkedList();

    public Request getRequest() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();//当线程被中断，应该返回null值，或者抛出异常
                    return null;
                }
            }
            return queue.removeFirst();
        }
    }

    public void putRequest(Request request) {
        synchronized (queue) {
            queue.addLast(request);
            queue.notifyAll();
        }

    }
}

package dpart8;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟客户端：
 * <p>
 * 客户端支持多个，可以允许在运行时添加请求，用了消费者和生产者的设计
 * 模式，将请求作为仓库，发送线程一直在运行，从仓库中获取请求。所以client使用
 * 两种设计模式，分别是消费者-生产者模式（用于解决发送消息机制）和
 * Guarded Suspension设计模式（用于服务器处理客户端的请求）。
 *
 * @author: mahao
 * @date: 2019/9/1
 */
public class Client extends Thread {

    private RequestQueue queue;

    //生产者和消费者解决客户端发送信息问题
    private final List<Request> list = new ArrayList<>();
    private Object lock = new Object();

    public Client(RequestQueue queue) {
        this.queue = queue;
    }

    //生产者
    public void sendMsg(String msg) {
        synchronized (lock) {
            while (list.size() > 100) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;//执行结束
                }
            }
            list.add(new Request(msg));
            lock.notifyAll();
        }

    }

    //消费者
    @Override
    public void run() {
        while (!isInterrupted()) {
            synchronized (lock) {
                while (list.isEmpty()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        interrupt();
                        System.out.println("检测到中断，已经中断");
                        return;
                    }
                }
                queue.putRequest(list.remove(0));
                lock.notifyAll();
            }
        }
        System.out.println("发送线程结束");
    }

    public void shutdown() {
        interrupt();
    }
}

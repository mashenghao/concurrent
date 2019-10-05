package channel;

/**
 * Worker-Thread设计模式；该设计模式的思想是，有工作，工人去做，没有工作，
 * 则会停下来等待工作到来。所以，根据介绍，该模式需要工作类，工人线程，以及
 * 负责运输工作的，对于工作还需要有人创造。
 * 根据介绍很像生产者和消费者的设计模式，工作就是产品，工人是消费者，运输工作的是
 * 请求队列。但是有不同，worker-Thread模式，负责管理了工人线程池，工人又执行了请求中
 * 的方法。
 *
 * <p>
 * Channel传输带，是设计模式的核心类，负责管理产品，和工人线程池。
 *
 * @author: mahao
 * @date: 2019/9/4
 */
public class Channel {

    //允许接收的最大请求数
    private static final int MAX_SIZE = 100;
    //定义所有的请求，作为请求队列，由Channel维护
    private final Request[] requestQueue;
    private int head;
    private int tail;

    private final WorkThread[] workThreads;


    public Channel(int works) {
        this.requestQueue = new Request[MAX_SIZE];
        workThreads = new WorkThread[works];
        init();
    }

    //初始化工作线程
    private void init() {
        for (int i = 0; i < workThreads.length; i++) {
            workThreads[i] = new WorkThread("worker-" + i, this);

        }
    }

    // 开启工作,工作内容就是从Channel中去请求
    public void startWorkers() {
        for (int i = 0; i < workThreads.length; i++) {
            workThreads[i].start();
        }
    }

    public synchronized Request take() {
        while (head == tail) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Request request = requestQueue[head];
        head = (head + 1) % requestQueue.length;
        notifyAll();
        return request;
    }

    public synchronized void put(Request request) {
        while ((tail + 1) % requestQueue.length == head) {//循环队列满
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        requestQueue[tail] = request;
        tail = (tail + 1) % requestQueue.length;
        notifyAll();
    }
}

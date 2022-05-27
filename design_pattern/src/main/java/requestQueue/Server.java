package requestQueue;

import chapter3_methods.ThreadUtil;

/**
 * 模拟服务器处理请求,继承Thread类，作为可以启动多个服务线程
 *
 * @author: mahao
 * @date: 2019/9/1
 */
public class Server extends Thread {

    private RequestQueue queue;
    private volatile boolean done = false;

    public Server(RequestQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!done) {
            Request request = queue.getRequest();
            String msg = request.getMsg();
            ThreadUtil.sleep(1000);//模拟处理请求
            System.out.println(ThreadUtil.getName() + "  " + msg);
        }

    }

    public void shutdown() {
        done = true;
    }
}

package requestQueue;

import chapter3_methods.ThreadUtil;

/**
 * Guarded Suspension 设计模式，
 * 服务器设置请求队列，处理大量请求的设计模式，可以理解为创建一个
 * 阻塞队列，客户端传入请求，服务端取请求，当服务端的请求为空时，等待请求
 *
 * @author: mahao
 * @date: 2019/9/1
 */
public class MainClass {

    public static void main(String[] args) {
        RequestQueue queue = new RequestQueue();
        Client client1 = new Client(queue);
        Server server1 = new Server(queue);
        Server server2 = new Server(queue);
        client1.start();
        server1.start();
        server2.start();
        for (int i = 0; i < 100; i++) {
            client1.sendMsg("msg-" + i);
            System.out.println(i);
        }
        ThreadUtil.sleep(100);
        client1.shutdown();
        ThreadUtil.sleep(5 * 1000);
        server2.shutdown();
    }
}

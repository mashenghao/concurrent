package dpart15;

/**
 * 测试类：
 *
 * @author: mahao
 * @date: 2019/9/4
 */
public class Client {

    public static void main(String[] args) {
        Channel channel = new Channel(5);
        channel.startWorkers();

        ClientThread clientThread = new ClientThread(channel);
        clientThread.start();
        ClientThread clientThread2 = new ClientThread(channel);
        clientThread2.start();
    }
}

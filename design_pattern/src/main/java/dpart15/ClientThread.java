package dpart15;

import java.util.Random;

/**
 * 客户端线程：
 * 负责向传输带上输送产品
 *
 * @author: mahao
 * @date: 2019/9/4
 */
public class ClientThread extends Thread {

    private final Channel channel;
    private static final Random random = new Random();


    public ClientThread(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        for (int i = 0; ; i++) {
            Request request = new Request(getName(), i);
            channel.put(request);
            
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

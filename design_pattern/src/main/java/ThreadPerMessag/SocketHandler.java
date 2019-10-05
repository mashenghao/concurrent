package ThreadPerMessag;

import chapter3.ThreadUtil;
import chapter8.ThreadPool3;

import java.net.Socket;

/**
 * @author: mahao
 * @date: 2019/9/3
 */
public class SocketHandler {

    //使用线程池优化
    ThreadPool3 threadPool = new ThreadPool3();

    public void deal(Socket socket) {

        threadPool.submit(() -> {
            int port = socket.getPort();
            ThreadUtil.sleep(1000);
            System.out.println("SocketHandler.deal # port is : " + port + " thread is :" + ThreadUtil.getName());
        });

        /*new Thread() {
            @Override
            public void run() {
                int port = socket.getPort();
                ThreadUtil.sleep(1000);
                System.out.println("SocketHandler.deal # port is : " + port + " thread is :" + ThreadUtil.getName());
            }
        }.start();*/
        
    }

}

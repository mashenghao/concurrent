package TwoPhaseTermination;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用两段式模式去负责清理工作。
 * <p>
 * 案例说明：
 * 通过Socket搭建一个客户端对服务端的会话程序，对于每个客户端，服务端
 * 使用了Thread-pre-Message设计模式去处理会话。服务端用Two Phase Termination来
 * 负责资源清理，包括服务程序关闭，以及处理会话的线程的关闭。
 *
 * @author: mahao
 * @date: 2019/9/4
 */
public class AppServer extends Thread {

    private final int port;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);//用于执行Sockethanler
    private List<SocketHandler> handlers = new ArrayList<>();//用于保存处理器
    private volatile boolean end = false;
    ServerSocket serverSocket = null;

    public AppServer(int port) {
        this.port = port;
    }


    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("AppServer.run # start server success");
            while (!end) {
                Socket socket = serverSocket.accept();
                System.out.println("AppServer.run # accept socket");
                SocketHandler socketHandler = new SocketHandler(socket);
                handlers.add(socketHandler);
                executorService.submit(socketHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.dispose();
        }

    }

    public void shutdown() {
        end = true;
        this.interrupt();
        //服务员socket
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁后回收资源，主要包括，服务器线程，线程池，客户端socket
     */
    private void dispose() {
        System.out.println("AppServer.dispose # dispose the server thread ... ");
        //关闭客户端socket
        for (SocketHandler handler : handlers) {
            handler.shutdown();
        }
        //关闭线程池
        executorService.shutdown();

    }
}

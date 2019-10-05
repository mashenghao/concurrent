package ThreadPerMessag;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: mahao
 * @date: 2019/9/3
 */
public class Server {

    public static void main(String[] args) throws IOException {

        SocketHandler socketHandler = new SocketHandler();
        ServerSocket serverSocket = new ServerSocket(8000);

        while (true) {
            Socket socket = serverSocket.accept();
            socketHandler.deal(socket);
        }
    }
}

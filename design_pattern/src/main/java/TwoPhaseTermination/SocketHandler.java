package TwoPhaseTermination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 处理一个socket的请求类，只负责打印客户端请求，并返回数据。
 *
 * @author: mahao
 * @date: 2019/9/4
 */
public class SocketHandler implements Runnable {

    private Socket socket;
    //会话结束标记，触发条件是，服务端客户端主动关闭，或者出现异常关闭。
    private volatile boolean end = false;


    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            String line = null;
            while (!end) {
                line = reader.readLine();
                if (line == null)
                    return;
                System.out.println("SocketHandler.run # accept message : " + line);

                writer.write("server return message for " + line);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            end = false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void shutdown() {
        if (end) {
            return;
        }
        end = true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

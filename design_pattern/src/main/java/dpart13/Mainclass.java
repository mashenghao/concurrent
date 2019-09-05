package dpart13;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Thread-Per-Messag设计模式，每个线程负责处理一个请求。(可以用线程池来优化)。
 * 应用案例：
 * 使用Thread-per-Message在服务端处理socket请求。
 *
 * @author: mahao
 * @date: 2019/9/3
 */
public class Mainclass {

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 10; i++) {
            Socket s = new Socket("localhost", 8000);
            InputStream in = s.getInputStream();
            System.out.println(in);
        }
    }
}

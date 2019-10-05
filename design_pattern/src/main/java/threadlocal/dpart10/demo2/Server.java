package threadlocal.dpart10.demo2;

import chapter3.ThreadUtil;

import java.util.Map;

/**
 * 服务器，用于启动程序
 *
 * @author: mahao
 * @date: 2019/10/3
 */
public class Server {

    public static void main(String[] args) {
        App app = new App();
        app.start();
        ThreadUtil.sleep(1000);
        Map<String, Object> session = app.getContext().getSession();

        App app2 = new App();
        app2.start();
        ThreadUtil.sleep(1000);
        Map<String, Object> session2 = app2.getContext().getSession();

        System.out.println(session == session2);
    }
}

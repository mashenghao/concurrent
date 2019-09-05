package dpart14;

import chapter3.ThreadUtil;

import java.util.AbstractSequentialList;

/**
 * @author: mahao
 * @date: 2019/9/4
 */
public class AppClient {

    public static void main(String[] args) {
        AppServer appServer = new AppServer(8000);
        appServer.start();
        System.out.println(111);

        ThreadUtil.sleep(10000);
        appServer.shutdown();
        System.out.println("关闭**************");
    }
}

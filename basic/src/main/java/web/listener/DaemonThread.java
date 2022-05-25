package web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 测试在web环境中，当服务器已经停止，但是其他线程仍然在继续运行的情况，
 * 以及守护线程在这种情况下的运用。
 * https://blog.csdn.net/shimiso/article/details/8964414
 *
 * @author: mahao
 * @date: 2019/8/10
 */
@WebListener
public class DaemonThread implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("init start ...");

        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("用户线程运行 ....");
                    try {
                        Thread.sleep(3_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        t.setDaemon(true);
        t.start();
        System.out.println("init end ...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("application destroy ...");
    }
}

package chapter7;

import chapter3_methods.ThreadUtil;

/**
 * @author: mahao
 * @date: 2019/8/14
 */
public class BThreadGroupApi {

    public static void main(String[] args) {
        ThreadGroup tg1 = new ThreadGroup("tg1");
        new Thread(tg1, "t1") {
            @Override
            public void run() {
                while (true) {

                }
            }
        }.start();

        ThreadGroup tg2 = new ThreadGroup(tg1, "tg2");
        new Thread(tg2, "t2") {
            @Override
            public void run() {
                while (true) {

                }
            }
        }.start();

        ThreadUtil.sleep(1000);
        System.out.println("tg1中活跃的线程数--" + tg1.activeCount());
        System.out.println("在tg2中创建线程，tg1线程s的活跃数是" + tg1.activeCount());
        System.out.println("tg1的子线程组"+ tg1.activeGroupCount());
    }

}

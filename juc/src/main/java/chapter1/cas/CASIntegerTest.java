package chapter1.cas;

import chapter1.ThreadUtil;

/**
 * @author: mahao
 * @date: 2019/9/21
 */
public class CASIntegerTest {

    public static void main(String[] args) {
        final CASInteger atomic = new CASInteger();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    int v1 = atomic.getAndChange(2);
                    ThreadUtil.sleep(10);
                    int v2 = atomic.getValue();
                    System.out.println(ThreadUtil.getName() + "__" + v1 + "    " + v2);
                }

            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                for (int j = 0; j < 10; j++) {
                    int v1 = atomic.getAndChange(2);
                    int v2 = atomic.getValue();
                    System.out.println(ThreadUtil.getName() + "__" + v1 + "    " + v2);
                }

            }
        }.start();

    }
}

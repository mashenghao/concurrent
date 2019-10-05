package utils.exchanger;

import atomic.ThreadUtil;

import java.util.concurrent.Exchanger;

/**
 * 用于两个线程之间交换数据，如果线程不是两个，则会出现混乱。
 *
 *
 * @author: mahao
 * @date: 2019/9/23
 */
public class Exchange {

    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<String>();

        new Thread() {
            @Override
            public void run() {
                while (true){
                    try {
                        ThreadUtil.sleep(3000);
                        String exchange = exchanger.exchange("this is thread A message");
                        System.out.println("线程A： " + exchange);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();


        new Thread() {
            @Override
            public void run() {
                while (true){
                    try {
                        String exchange = exchanger.exchange("this is thread B message");
                        System.out.println("线程B： " + exchange);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                while (true){
                    try {
                        ThreadUtil.sleep(1000);
                        String exchange = exchanger.exchange("this is thread C message");
                        System.out.println("线程C： " + exchange);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }
}

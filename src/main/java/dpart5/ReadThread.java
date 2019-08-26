package dpart5;

import chapter3.ThreadUtil;

/**
 * 读线程，对一个共享数据（字符数组），进行读取，复制到本地线程，可以开取多个线程
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class ReadThread extends Thread {

    private ShareData data;

    public ReadThread(ShareData data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char[] buf = data.readData();
                System.out.println("read  " + ThreadUtil.getName() + "  " + String.valueOf(buf));
                //ThreadUtil.sleep(1000);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

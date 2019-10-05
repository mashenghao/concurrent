package readWriteLock;

import chapter3.ThreadUtil;

/**
 * 写线程
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class WriterThread extends Thread {

    private String filler;
    private ShareData data;

    public WriterThread(String filler, ShareData data) {
        this.data = data;
        this.filler = filler;
    }

    @Override
    public void run() {
        //对一个线程一直进行写操作
        try {
            while (true) {
                char ch = nextChar();
                data.writerData(ch);
                System.out.println("writer  " + ThreadUtil.getName() + "  " + data.getData());
                //ThreadUtil.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int i = 0;

    private char nextChar() {
        i++;
        if (i >= filler.length()) {
            i = 0;
        }
        return filler.charAt(i);
    }


}

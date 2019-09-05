package dpart5;

/**
 * @author: mahao
 * @date: 2019/8/26
 */
public class MainClass {

    public static void main(String[] args) {
        ShareData data = new ShareData();
        new ReadThread(data).start();
        new ReadThread(data).start();
        new ReadThread(data).start();
        new ReadThread(data).start();
        new ReadThread(data).start();
        new WriterThread("123456", data).start();
        new WriterThread("abcdef", data).start();
        //new WriterThread("/*-+.?", data).start();
    }
}

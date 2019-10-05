package readWriteLock;

/**
 * 操作共享数据，进行了线程同步操作
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class ShareData {

    private final char[] buff;

    private final ReadWriteLock lock = new ReadWriteLock(true);//读写锁

    public ShareData() {
        buff = new char[10];
        for (int i = 0; i < 10; i++) {
            buff[i] = '*';
        }
    }

    //对共享数据，进行拷贝操作
    public char[] readData() throws InterruptedException {
        try {
            lock.readLock();
            /*
            获得读的锁后，进行数据的读取操作
             */

            return doRead();
        } finally {
            lock.unReadLock();
        }
    }

    public void writerData(char c) throws InterruptedException {
        try {
            lock.writerLock();
            doWriter(c);
        } finally {
            lock.unWriterLock();
        }
    }

    private void doWriter(char c) {
        for (int i = 0; i < buff.length; i++) {
            buff[i] = c;
        }
    }

    private char[] doRead() {
        char[] copyBuf = new char[10];
        for (int i = 0; i < buff.length; i++) {
            //验证读写锁是否成立，如果不成立，则拷贝的数组会有其他元素
            copyBuf[i] = buff[i];
        }

        return copyBuf;
    }


    public String getData() {
        return String.valueOf(buff);
    }
}

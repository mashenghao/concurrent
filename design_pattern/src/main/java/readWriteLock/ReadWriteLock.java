package readWriteLock;

/**
 * 读写锁的实现:
 * <p>
 * 如果对wait和notify操作不理解，可以先去看BooleanLock的实现。
 * 1.对于读锁，只需要判断是否有写线程在访问资源，如果存在，则线程进入wait状态，
 * 如果只有读线程，或者无线程，则可以直接获取到锁。 之后，对锁内读线程的数量++操作，
 * 表示，读的线程数。
 * <p>
 * 2.对于写锁，是和读线程和写线程同时互斥的，他需要满足，没有任何线线程正在访问共享资源，判断的
 * 条件及时，对资源的读线程数量和写线程数量。
 * <p>
 * 3.设置写操作优先，通知设置boolean变量preferWriter，来判断是否设置了这一规则，如果设置了，则让
 * 读线程陷入等待的条件扩大，之前是只需要判断是否存在写线程，现在也要判断是否存在等待的写线程。
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public class ReadWriteLock {

    //正在进行读线程的数量
    private int readerCount = 0;
    //写线程的数量，最大只能是1
    private int writerCount = 0;

    //分别正在陷入读写锁等待的线程
    private int waitReader = 0;
    private int waitWriter = 0;

    //给写线程设置优先级高,默认是优先级高的
    private boolean preferWriter = true;

    public ReadWriteLock(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    //获取读锁
    public synchronized void readLock() throws InterruptedException {
        this.waitReader++;
        try {
            //通过这个判断，判断存在写等待线程的个数，存在了就也让读进程等待
            while (this.writerCount > 0 || (preferWriter && this.waitWriter > 0)) {
                this.wait();
            }

            this.readerCount++;//读线程++
        } finally {
            this.waitReader--;//获取到了读锁，则正在等待读的线程减少
        }
    }

    //释放读锁
    public synchronized void unReadLock() {
        this.readerCount--;
        this.notifyAll();//唤醒等待的写线程
    }

    //获取写锁
    public synchronized void writerLock() throws InterruptedException {
        this.waitWriter++;
        try {
            while (this.readerCount > 0 || this.writerCount > 0) {//当无读线程和写线程时
                this.wait();
            }

            this.writerCount++;//写锁++
        } finally {
            this.waitWriter--;
        }
    }

    //释放写锁
    public synchronized void unWriterLock() {
        this.writerCount--;
        this.notifyAll();//唤醒读写线程
    }

}

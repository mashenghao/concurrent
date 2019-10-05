package Volatile;

import chapter3.ThreadUtil;

/**
 * 创建两个线程，共享一个变量，查看java运算，会将处于主存的共享变量拷贝到
 * 本地内存中，这样其他线程对此数据改变是不可预知的。
 *
 * java内存模型的设计使得所有的变量都存在在主内存中，每个线程都有自己的工作内存，线程的工作内存中
 * 保存了变量主内存的副本，线程对变量的所有操作都必须在工作内存中完成，不能直接访问工作内存。不同线程直接的
 * 工作内存不能相互访问。存在的问题是，在多线程的环境下，多个工作内存中存在着同一个变量的主内存副本，当一个更新后，
 * 将数据刷新会主内存，则其他线程无法感知，则会造成并发问题。对于案例，读线程中，工作内存中有count的拷贝，当写线程发生
 * 更改，在工作内存中，读线程无法感知到变化，使用的还是线程开始的数据count。所以会一直发生死循环，由于读线程没有发生任何
 * 对数据的写操作，jvm认为不需要更新数据，每次数据只需要从工作内存中获取。
 * 对于写线程，有对变量的写操作，会刷新cache，案例看Volatile2.java
 *
 * @author: mahao
 * @date: 2019/8/21
 */
public class Volatile1 {

    private static final int MAX_LIMIT = 5;

    private static int count = 0;

    /**
     * 创建两个线程，一个修改共享变量数据，另一个感知数据变化；
     * <p>
     * 结果分析：
     * 当共享变量上不加volatile关键字，输出结果只有更新的日志，读取线程则没有感知到数据变化，因为每个线程都有自己的
     * 工作内存，线程将数据拷贝到工作内存中，在工作内存中进行运算，之后将结果刷新到主存中。由于写线程的数据变化，一直在
     * 自己的工作内存中，所以读进程无法察觉数据变化，才会一直无输出。加入volatile后，会将工作内存中的数据，都更新到主存中，
     * 所以运算时透明锝。
     *
     * @param args
     */
    public static void main(String[] args) {

        Thread readThread = new Thread() {
            @Override
            public void run() {
                int localValue = count;
                while (localValue < MAX_LIMIT) {
                    if (count != localValue) {
                        System.out.println("the num be change is " + count);
                        localValue = count;
                    }
                }
            }
        };
        readThread.start();

        Thread writerThread = new Thread() {
            @Override
            public void run() {
                int localValue = count;
                while (localValue < MAX_LIMIT) {
                    System.out.println("the num be update is " + ++localValue);
                    count = localValue;
                    ThreadUtil.sleep(500);
                }
            }
        };
        writerThread.start();

    }
}

package chapter5_produceConsumer;

import chapter3_methods.ThreadUtil;

/**
 * 生产者消费者模式2,使用线程通信方式：
 * <p>
 * 线程1生产的商品让线程2消费，消费完后，才能生产
 * 线程2消费的商品，让线程1生产，只有生产了才能消费。
 *
 * @author: mahao
 * @date: 2019/8/12
 */
public class ProduceConsumer2 {

    int i=0;
    byte[] lock = new byte[1];

    public void produce() {
        while (true)
            synchronized (lock) {
                ThreadUtil.sleep(1000);
                if (i < 1) {//仓库未满，进行生产 当i=1是，两个线程交互执行
                    i++;
                    System.out.println("P-->" + i);
                    lock.notify();//生产完毕后，唤醒要消费者的 阻塞线程
                } else {
                    //已经生产完毕了，则要让当前线程等待状态，等待到消费线程先去消费
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

    }

    public void consumer() {
        while (true)
            synchronized (lock) {
                ThreadUtil.sleep(1000);
                if (i > 0) {//仓库中存在商品，则直接消费
                    System.out.println("C-->" + i);
                    i--;
                    lock.notify();//仓库有空余，通过生产者生产
                } else {
                    //仓库中没有商品了，则要阻塞等待生产
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    public static void main(String[] args) {
        ProduceConsumer2 produceConsumer = new ProduceConsumer2();
        ThreadUtil.execute(() -> {
            produceConsumer.produce();
        });

        ThreadUtil.execute(() -> {
            produceConsumer.consumer();
        });
    }
}

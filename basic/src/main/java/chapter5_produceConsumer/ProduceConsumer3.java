package chapter5_produceConsumer;

import chapter3_methods.ThreadUtil;

/**
 * 生产者消费者模式3
 *
 * @author: mahao
 * @date: 2019/8/12
 */
public class ProduceConsumer3 {

    int i = 0;
    byte[] lock = new byte[1];

    public void produce() {
        while (true) {
            synchronized (lock) {
                while (i > 0) { // wait会调用后，会释放锁，导致下次在获取到锁的时候，会将以前的判断逻辑错过，所以用while
                    //已经生产完毕了，则要让当前线程等待状态，等待到消费线程先去消费
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                i++;
                System.out.println(Thread.currentThread().getName() + "P-->" + i);
                lock.notifyAll();//生产完毕后，唤醒要消费者的 阻塞线程

            }
        }
    }

    public void consumer() {
        while (true) {
            synchronized (lock) {
                while (i < 1) {
                    //仓库中没有商品了，则要阻塞等待生产
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(Thread.currentThread().getName() + "C-->" + i);
                i--;
                lock.notifyAll();//仓库有空余，通过生产者生产

            }
        }

    }

    public static void main(String[] args) {
        ProduceConsumer3 produceConsumer = new ProduceConsumer3();
        ThreadUtil.execute(() -> {
            produceConsumer.produce();
        });
        ThreadUtil.execute(() -> {
            produceConsumer.produce();
        });

        ThreadUtil.execute(() -> {
            produceConsumer.consumer();
        });
        ThreadUtil.execute(() -> {
            produceConsumer.consumer();
        });
    }
}

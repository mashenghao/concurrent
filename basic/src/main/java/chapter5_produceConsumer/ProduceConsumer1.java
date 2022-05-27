package chapter5_produceConsumer;

import chapter3_methods.ThreadUtil;

/**
 * 生产者消费者模式
 *
 * @author: mahao
 * @date: 2019/8/12
 */
public class ProduceConsumer1 {

    int i;
    byte[] lock = new byte[1];

    public void produce() {
        while (true)
            synchronized (lock) {

                if (i < 5) {
                    i++;
                    System.out.println("上产了" + i);
                }
            }

    }

    public void consumer() {
        while (true)
            synchronized (lock) {

                if (i >= 1) {
                    System.out.println("消费了" + i);
                    i--;
                }
            }
    }

    public static void main(String[] args) {
        ProduceConsumer1 produceConsumer = new ProduceConsumer1();
        ThreadUtil.execute(() -> {
            produceConsumer.produce();
        });

        ThreadUtil.execute(() -> {
            produceConsumer.consumer();
        });
    }
}

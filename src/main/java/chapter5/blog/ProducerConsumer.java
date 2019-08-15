package chapter5.blog;

import chapter3.ThreadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: mahao
 * @date: 2019/8/13
 */
public class ProducerConsumer {
    public static final int MAX_SIZE = 1;

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        ThreadUtil.execute(new Producter(list));
        ThreadUtil.execute(new Producter(list));
        ThreadUtil.execute(new Producter(list));
        ThreadUtil.execute(new Consumer(list));
        ThreadUtil.execute(new Consumer(list));
    }
}

class Producter implements Runnable {

    private List<String> list;//仓库

    public Producter(List<String> list) {
        this.list = list;
    }

    public void run() {
        while (true) {
            synchronized (list) {
                while (list.size() >= ProducerConsumer.MAX_SIZE) {//生产者中，仓库满了，陷入等待
                    try {
                        System.out.println("生产者" + Thread.currentThread().getName() + "  list以达到最大容量，进行wait");
                        list.wait();
                        System.out.println("生产者" + Thread.currentThread().getName() + "  退出wait");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Random random = new Random();
                int i = random.nextInt();
                System.out.println("生产者" + Thread.currentThread().getName() + " 生产数据" + i);
                list.add(i + "");
                list.notifyAll();
            }
        }
    }
}

//消费者
class Consumer implements Runnable {

    private List<String> list;//仓库

    public Consumer(List<String> list) {
        this.list = list;
    }

    public void run() {
        try {
            while (true) {
                synchronized (list) {
                    while (list.isEmpty()) {
                        System.out.println("消费者" + Thread.currentThread().getName() + "  list为空，进行wait");
                        list.wait();
                        System.out.println("消费者" + Thread.currentThread().getName() + "  退出wait");
                    }

                    String element = list.remove(0);
                    System.out.println("消费者" + Thread.currentThread().getName() + "  消费数据：" + element);
                    list.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
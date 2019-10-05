package collection;

import atomic.ThreadUtil;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 非阻塞队列实现原理具体我也不知，没看懂
 *
 * @author: mahao
 * @date: 2019/10/4
 */
public class ConcurrentLinkedQueue1 {


    public static void main(String[] args) {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Integer poll = queue.poll();
                System.out.println("poll: " + poll);
                ThreadUtil.randSleep(10, 100);
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Integer poll = queue.poll();
                System.out.println("poll: " + poll);
                ThreadUtil.randSleep(10, 100);
            }
        }).start();
        for (int i = 0; i < 10; i++) {
            queue.add(i);
            System.out.println("offer: " + i);
            // ThreadUtil.randSleep(10);
        }

    }
}

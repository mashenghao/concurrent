package collection.blockQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ArrayBlockingQueue 的 api:
 * 1.有界队列
 * 2.put 和 take 会阻塞
 * 3.poll 和 offer 直接返回执行结果
 *
 * @author: mahao
 * @date: 2019/10/6
 */
public class ArrayBlockingQueue2 {

    /**
     * 具体可以参看源码，代码不难，使用是线程安全的，都是用了ReentrantLock来锁住操作。
     */
    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        queue.add("1");
        queue.add("2");
        queue.add("3");
        //如果是使用put，满了后则会发生阻塞
        queue.put("4");
        //当队列满了后，会抛出异常
        queue.add("4");

    }


}

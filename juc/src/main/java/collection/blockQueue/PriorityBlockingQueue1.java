package collection.blockQueue;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * PriorityBlockingQueue和Arraylist是一致得，是排序的队列，实现是插入的时候，
 * 排序插入。因为是有序的插入，所以元素必须是可以排序的（Comparable子类）。
 *
 * @author: mahao
 * @date: 2019/10/6
 */
public class PriorityBlockingQueue1 {

    private PriorityBlockingQueue<String> queue;

    @Before
    public void set() {
        queue = create(3);
    }

    /**
     * offer add put 都是调用的offer方法，因为是扩容数组，所以不会
     * 发生阻塞。
     */
    @Test
    public void testAdd() {
        for (int i = 0; i < 10; i++) {
            queue.offer(i + "");
        }
        System.out.println(queue);
    }


    /**
     * 出队列，这时候take会陷入阻塞，但是poll会直接返回
     */
    @Test
    public void testRemove() throws InterruptedException {
        String poll = queue.poll();
        System.out.println(poll);
        System.out.println(queue.take());
    }


    /***
     * 排序
     * @throws InterruptedException
     */
    @Test
    public void testComparetor() throws InterruptedException {
        PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(10, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }


    public static <T> PriorityBlockingQueue<T> create(int size) {
        return new PriorityBlockingQueue<>(size);
    }


}

package collection.list;


import atomic.ThreadUtil;
import collection.list.src.CopyOnWriteArrayList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CopyOnWriteArrayList解决了list的集合并发问题，处理方式是对于写操作，将原数组进行复制，
 * 然后加锁操作，当操作完毕后，将集合的数组指向新的数组。所以，在迭代的时候，也不大发生其他线程并发修改带来的问题，
 * 因为遍历的是之前的那个未经过修改的数据。
 *
 * @author: mahao
 * @date: 2019/10/4
 */
public class CopyOnWriteArrayList1 {


    private static List<String> list = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {

        list.add("1");
        list.add("2");
        list.add("3");


        // 存放10个线程的线程池
        ExecutorService service = Executors.newFixedThreadPool(10);
        new Thread() {
            @Override
            public void run() {
                // 执行10个任务(我当前正在迭代集合（这里模拟并发中读取某一list的场景）)
                for (int i = 0; i < 10; i++) {
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            Iterator<String> iter = list.iterator();
                            System.out.println("read --->" + ThreadUtil.getName());

                            while (iter.hasNext()) {
                                System.err.print(iter.next() + ",");
                            }
                            System.out.println();
                        }
                    });
                    ThreadUtil.sleep(1000);
                }
            }
        }.start();


        // 执行10个任务
        for (int i = 0; i < 10; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    list.add("121");// 添加数据
                    System.out.println("write --->" + ThreadUtil.getName());
                }
            });
            ThreadUtil.sleep(1000);

        }

        System.err.println(Arrays.toString(list.toArray()));

        service.awaitTermination(1, TimeUnit.HOURS);
        service.shutdown();

    }

}

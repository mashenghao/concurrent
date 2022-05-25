package collection.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: mahao
 * @date: 2019/10/6
 */
public class ConcurrentHashMap1 {

    /**
     * start the map [ java.util.Hashtable ], use thread num : 5
     * 2500000 element be insert , the time is : 725
     * 2500000 element be insert , the time is : 613
     * 2500000 element be insert , the time is : 529
     * 2500000 element be insert , the time is : 529
     * 2500000 element be insert , the time is : 530
     * ================java.util.Hashtable: 585============================
     * start the map [ java.util.Hashtable ], use thread num : 5
     * 2500000 element be insert , the time is : 922
     * 2500000 element be insert , the time is : 1068
     * 2500000 element be insert , the time is : 939
     * 2500000 element be insert , the time is : 1081
     * 2500000 element be insert , the time is : 976
     * ================java.util.Hashtable: 997============================
     * start the map [ java.util.Collections$SynchronizedMap ], use thread num : 5
     * 2500000 element be insert , the time is : 680
     * 2500000 element be insert , the time is : 562
     * 2500000 element be insert , the time is : 569
     * 2500000 element be insert , the time is : 568
     * 2500000 element be insert , the time is : 563
     * ================java.util.Collections$SynchronizedMap: 588============================
     * start the map [ java.util.Collections$SynchronizedMap ], use thread num : 5
     * 2500000 element be insert , the time is : 910
     * 2500000 element be insert , the time is : 926
     * 2500000 element be insert , the time is : 896
     * 2500000 element be insert , the time is : 911
     * 2500000 element be insert , the time is : 910
     * ================java.util.Collections$SynchronizedMap: 910============================
     * start the map [ java.util.concurrent.ConcurrentHashMap ], use thread num : 5
     * 2500000 element be insert , the time is : 500
     * 2500000 element be insert , the time is : 385
     * 2500000 element be insert , the time is : 405
     * 2500000 element be insert , the time is : 405
     * 2500000 element be insert , the time is : 402
     * ================java.util.concurrent.ConcurrentHashMap: 419============================
     * start the map [ java.util.concurrent.ConcurrentHashMap ], use thread num : 5
     * 2500000 element be insert , the time is : 403
     * 2500000 element be insert , the time is : 367
     * 2500000 element be insert , the time is : 376
     * 2500000 element be insert , the time is : 382
     * 2500000 element be insert , the time is : 388
     * ================java.util.concurrent.ConcurrentHashMap: 383============================
     * <p>
     * Process finished with exit code 0
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> map1 = new Hashtable<>();
        Map<String, Integer> map2 = Collections.synchronizedMap(new HashMap<>());
        Map<String, Integer> map3 = new ConcurrentHashMap<>();

        final int threshold = 1;
        pressureMap(new Hashtable<>(), threshold, false);
        pressureMap(new Hashtable<>(), threshold, true);

        pressureMap(Collections.synchronizedMap(new HashMap<>()), threshold, false);
        pressureMap(Collections.synchronizedMap(new HashMap<>()), threshold, true);


        pressureMap(new ConcurrentHashMap<>(), threshold, false);
        pressureMap(new ConcurrentHashMap<>(), threshold, true);

    }

    /**
     * 测试map的并发性能
     *
     * @param map       线程安全的map
     * @param threshold 线程数
     * @param reds      是否读
     */
    public static void pressureMap(Map map, int threshold, boolean reds) throws InterruptedException {
        System.out.printf("start the map [ %s ], use thread num : %d \n", map.getClass().getName(), threshold);
        long total = 0;
        final int MAX_NUM = 500000;
        for (int i = 0; i < 5; i++) {
            //执行一次MAX_NUM次数的map的测试
            ExecutorService service = Executors.newFixedThreadPool(threshold);
            long start = System.nanoTime();
            for (int k = 0; k < threshold; k++) {//提交了threshold个测试任务
                service.execute(() -> { //每一个测试认识是插入num个元素
                    for (int j = 0; j < MAX_NUM; j++) {
                        Integer num = (int) Math.ceil(Math.random() * 10000);
                        if (reds)
                            map.get(String.valueOf(num));
                        map.put(String.valueOf(num), num);
                    }
                });
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.SECONDS);
            long end = System.nanoTime();
            long perior = (end - start) / 1000000;
            System.out.println(threshold * MAX_NUM + " element be insert , the time is : " + perior);
            total += perior;
        }
        System.out.printf("================%s: %d============================\n", map.getClass().getName(), total / 5);
    }
}

package collection.map;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 源码阅读；
 *
 * @author: mahao
 * @date: 2019/10/7
 */
public class ConcurrentHashMap2 {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap();
        for (int i = 0; i < 100; i++) {
            String put = map.put(i, i + "");
        }
        ConcurrentHashMap.KeySetView<Integer, String> s = map.keySet();
        System.out.println(s);

        Enumeration<Integer> keys = map.keys();
        while (keys.hasMoreElements()) {
            System.out.println(keys.nextElement());
        }
    }
}

package threadlocal.dpart9;

import java.util.HashMap;

/**
 * ThreadLocal的功能简易实现
 * <p>
 * 1.可以通过创建一个map，map的key存的是当前线程，v是值，这样也是不冲突的。
 * <p>
 * 2.通过在Thread设置属性，在每个线程中，设置当地值，这样也是可以完成的。用第二种方式演示
 *
 * @author: mahao
 * @date: 2019/9/2
 */
//实现思路如下，因为类型转换错误，尚未实现
public class ThreadLocalImpl<T> {


    public T get() {
        Thread t = Thread.currentThread();

        ThreadMapMap map = ((MyThread) t).threadMapMap;
        if (map != null) {
            return (T) map.get(this);
        }
        return null;
    }

    static class ThreadMapMap extends HashMap {

        public ThreadMapMap(ThreadLocalImpl tThreadLocal, Object val) {
            put(tThreadLocal, val);
        }
    }

    public void put(T val) {
        Thread t = Thread.currentThread();
        ThreadMapMap map = getMap((MyThread) t);
        if (map == null) {
            map = createMap(this, val);
        } else {
            map.put(this, val);
        }
    }

    private ThreadMapMap createMap(ThreadLocalImpl<T> tThreadLocal, T val) {
        return new ThreadMapMap(tThreadLocal, val);
    }

    private ThreadMapMap getMap(MyThread t) {
        return t.threadMapMap;
    }
}

class MyThread extends Thread {

    public ThreadLocalImpl.ThreadMapMap threadMapMap;

}
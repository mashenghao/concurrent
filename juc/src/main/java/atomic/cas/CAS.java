package atomic.cas;

import atomic.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS算法保证数据的原子性 （copareandSet），比较值符合当前结果值，
 * 然后进行赋值，否则，重新进行赋值。
 * <p>
 * for (;;) {
 *   int current = get();
 *   int next = current + delta;
 *   if (compareAndSet(current, next))
 *       return next;
 *  }
 * CAS算法的核心就是先获取atomic对象值，对值进行加值操作，然后比较最atomic新的值，
 * 是否和先前获取的值相同，如果相同，则证明没有其他线程修改，将进行过加值操作的结果返回；
 * 如果不同，则证明修改过，放弃这个新值。在进行循环获取，直到符合期望值和实际值相同为止。
 * compareAndSet是具有原子操作的，否则可能发生丢失修改。
 *
 * @author: mahao
 * @date: 2019/9/21
 */
public class CAS {

    public static void main(String[] args) {
         /*
        getAndSet的使用：通过比较，如果符合期望值，则进行值变更
        public final int getAndSet(int newValue) {
            for (;;) {
                int current = get();
                if (compareAndSet(current, newValue))
                    return current;
            }
        }
         */

        /*
        A B线程分别调用getAndSet方法，进行数值的增加
         */
        final AtomicInteger atomic = new AtomicInteger(0);

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    int v1 = atomic.addAndGet(1);
                    ThreadUtil.sleep(10);
                    int v2 = atomic.get();
                    System.out.println(ThreadUtil.getName() + "__" + v1 + "    " + v2);
                }

            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                for (int j = 0; j < 10; j++) {
                    int v1 = atomic.addAndGet(1);
                    int v2 = atomic.get();
                    System.out.println(ThreadUtil.getName() + "__" + v1 + "    " + v2);
                }

            }
        }.start();
    }

}

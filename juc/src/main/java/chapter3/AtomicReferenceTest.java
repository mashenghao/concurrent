package chapter3;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicReference测试：
 *
 * @author: mahao
 * @date: 2019/9/21
 */
public class AtomicReferenceTest {


    /**
     * AtomicReference的操作和实现原理和AtomicInteger是一致的，一个存储的数值一个是存储的是对象，
     * 当atomicReference进行修改时，如果本来值与期望值对象是同一个，则本来值换成更新值，否则循环继续。
     *
     * @param args
     */
    public static void main(String[] args) {

        Inner inner = new Inner("aa");
        AtomicReference<Inner> atomic = new AtomicReference<Inner>(inner);

        Inner bb = atomic.getAndSet(new Inner("bb"));
        System.out.println(bb);
        System.out.println(atomic.get());

    }


    private static class Inner {

        private String name;

        public Inner(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Inner{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}

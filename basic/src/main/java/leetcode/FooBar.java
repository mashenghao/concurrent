package leetcode;

/**
 * 我们提供一个类：
 * <p>
 * class FooBar {
 * public void foo() {
 *     for (int i = 0; i < n; i++) {
 *       print("foo");
 *     }
 * }
 * <p>
 * public void bar() {
 *     for (int i = 0; i < n; i++) {
 *       print("bar");
 *     }
 * }
 * }
 * 两个不同的线程将会共用一个 FooBar 实例。其中一个线程将会调用 foo() 方法，另一个线程将会调用 bar() 方法。
 * <p>
 * 请设计修改程序，以确保 "foobar" 被输出 n 次。
 * <p>
 *  
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-foobar-alternately
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class FooBar {
    private int n;
    boolean flag = true;

    public FooBar(int n) {
        this.n = n;
    }

    public synchronized void foo(Runnable printFoo) throws InterruptedException {
        while (flag) {
            this.wait();
        }
        for (int i = 0; i < n; i++) {
            printFoo.run();
            this.notify();
            this.wait();
        }
    }

    public synchronized void bar(Runnable printBar) throws InterruptedException {

        flag = false;
        this.notify();

        for (int i = 0; i < n; i++) {
            this.wait();
            printBar.run();
            this.notify();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {


            FooBar fooBar = new FooBar(10);
            Thread foo = new Thread() {
                @Override
                public void run() {
                    try {
                        fooBar.foo(() -> System.out.print("foo"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread bar = new Thread() {
                @Override
                public void run() {
                    try {
                        fooBar.bar(() -> System.out.print("bar"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            foo.start();
            bar.start();
            bar.join();
            System.out.println();
        }
    }
}

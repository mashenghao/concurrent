package leetcode;

import java.sql.SQLOutput;

/**
 * 三个不同的线程将会共用一个 Foo 实例。
 * <p>
 * 线程 A 将会调用 one() 方法
 * 线程 B 将会调用 two() 方法
 * 线程 C 将会调用 three() 方法
 * 请设计修改程序，以确保 two() 方法在 one() 方法之后被执行，three() 方法在 two() 方法之后被执行。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-in-order
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author: mahao
 * @date: 2019/9/3
 */
class Foo {

    int i = 0;

    public Foo() {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        synchronized (this) {
            printFirst.run();
            i = 2;
            this.notifyAll();
        }

    }

    public void second(Runnable printSecond) throws InterruptedException {
        synchronized (this) {
            while (i != 2) {
                this.wait();
            }
            printSecond.run();
            i = 3;
            this.notifyAll();
        }

    }

    public void third(Runnable printThird) throws InterruptedException {

        synchronized (this) {
            while (i != 3) {
                this.wait();
            }
        }
        printThird.run();
    }
}

class Test {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Foo foo = new Foo();
            Thread t1 = new Thread(() -> {
                try {
                    foo.first(() -> {
                        System.out.print(" one ");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    foo.second(() -> System.out.print(" two "));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            Thread t3 = new Thread(() -> {
                try {
                    foo.third(() -> System.out.print(" three "));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t1.start();
            t2.start();
            t3.start();
            
            t3.join();
            System.out.println();
        }

    }
}
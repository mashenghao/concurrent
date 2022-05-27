package chapter4_synchronized;

import chapter3_methods.ThreadUtil;


/**
 * 死锁检测分析:
 * 操作系统中死锁发生的条件：
 * 循环等待，不可抢占，请求与保持，资源互斥。
 * <p>
 * 1.jps 查看线程的id
 * 2.jstack 查看线程的堆栈信息，有关于线程的信息分析
 *
 * @author: mahao
 * @date: 2019/8/12
 */
public class DeadLock {

    public static void main(String[] args) {

        Class1 class1 = new Class1();
        Class2 class2 = new Class2();
        class1.setClass2(class2);
        class2.setClass1(class1);
        Thread t = new Thread() {
            @Override
            public void run() {
                class1.method1();
            }
        };
        t.start();

        class2.method1();
    }
}

//类一
class Class1 {

    private Class2 class2;

    public void setClass2(Class2 class2) {
        this.class2 = class2;
    }

    public void method1() {
        synchronized (this) {
            System.out.println("我是class1的method1，我要调用----》");
            ThreadUtil.sleep(100);
            class2.method2();
        }
    }

    public synchronized void method2() {
        System.out.println("class 1 ......");
    }
}

//类二
class Class2 {

    private Class1 class1;

    public void setClass1(Class1 class1) {
        this.class1 = class1;
    }

    public void method1() {
        synchronized (this) {
            System.out.println("我是class2的method1，我要调用----》");
            ThreadUtil.sleep(100);
            class1.method2();
        }
    }

    public synchronized void method2() {
        System.out.println("class 2  ......");
    }
}

class Class3 {


    private byte[] lock1 = new byte[1];
    private byte[] lock2 = new byte[1];

    public void m1() {
        synchronized (lock1) {
            System.out.println("m1方法获得了 lock1 锁");
            ThreadUtil.sleep(100);
            synchronized (lock2) {
                System.out.println("m1 方法获得了 lock2 锁");
            }
        }
    }

    public void m2() {
        synchronized (lock2) {
            System.out.println("m2方法获得了 lock2 锁");
            ThreadUtil.sleep(100);
            synchronized (lock1) {
                System.out.println("m2 方法获得了 lock1 锁");
            }
        }
    }

    public static void main(String[] args) {
        Class3 class3 = new Class3();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                class3.m1();
            }
        };
        t1.start();
        class3.m2();
    }
}
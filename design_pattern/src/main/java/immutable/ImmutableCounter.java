package immutable;

import chapter3_methods.ThreadUtil;

/**
 * 基于 “不可变类” 实现一个线程安全的 Integer 计数器
 * <p>
 * 仔细思考String的实现，他是线程安全的，并没有给String的操作加锁，
 * 为什么他是安全的，因为String类是不可变类，将类设置为final，属性也设置
 * 为final，对象不允许修改，如果线程对此修改了，则返回一个新的String对象，而
 * 原对象，仍然是不变的。
 * <p>
 * 按照String的实现，实现一个线程安全的Integer计数器。
 *
 * @author: mahao
 * @date: 2019/8/28
 */
public final class ImmutableCounter {

    /**
     * 定义为final类型，数值一旦被确定，则不允许改变
     */
    private final int count;

    public ImmutableCounter() {
        count = 0;
    }

    public ImmutableCounter(int i) {
        count = i;
    }

    public int getCount() {
        return count;
    }

    /**
     * 对数值计数器进行+1操作，实现是创建一个新的ImmutableCounter对象，数值
     * 是原数值+1。
     *
     * @return
     */
    public ImmutableCounter addCounter() {
        return new ImmutableCounter(this.getCount() + 1);
    }
}

class TestClass {

    /**
     * 创建三个线程，创建一个ImmutableCounter对象，让三个线程里面，每个线程调用3次自增的方法。
     * 因为这个对象是不可变对象，线程之间调用不干扰，自增的不是原对象，而是新生出来的对象，所以
     * 实验结果应该是三个线程中，每个线程里面的值，编程3
     *
     * @param args
     */
    public static void main(String[] args) {
        ImmutableCounter counter = new ImmutableCounter();
        for (int i = 0; i < 3; i++) {
            new Thread("t" + i) {
                @Override
                public void run() {
                    ImmutableCounter myCounter = counter;//指向的也是计数器对象
                    for (int j = 0; j < 3; j++) {
                        int oldVal = myCounter.getCount();
                        myCounter = myCounter.addCounter();
                        int newVal = myCounter.getCount();
                        System.out.println(getName() + " oldvalue: " + oldVal + " newValue: " + newVal);
                        ThreadUtil.sleep(5);
                    }
                }
            }.start();
        }
    }
}
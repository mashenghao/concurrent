package observe;

/**
 * 在java多线程模式下使用监听器设计模式，
 * <p>
 * 用来监听线程的生命周期
 */
public class MainClass {


    public static void main(String[] args) {

    }

    /**
     * 这是一个并发查询的方法
     */
    public void concurrentQuery() {
        LifeListener listener = new LifeListener();

        LifeObservableRunnable run1 = new LifeObservableRunnable(listener);

        new Thread(run1).start();
        new Thread(run1).start();
        new Thread(run1).start();
    }
}
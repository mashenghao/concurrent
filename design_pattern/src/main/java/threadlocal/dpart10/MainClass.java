package threadlocal.dpart10;

/**
 * ThreadLocal的使用，使用方式是替换全部都要来回传入的参数，比如在web中，
 * session等全局需要使用的，可以存在ThreadLocal中，struts2中的ActionContext的
 * 实现就是使用了ThreadLocal实现了。
 * <p>
 * 先创建一个案例，没有threadlocal实现ActionContext的功能。
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class MainClass {

    /**
     * 每个线程中都是单独创建的一个context，所以在多个线程中，每个线程都是使用的自己的context，不会发生
     * 冲突，但这种方法不足时，需要给每个方法都传递了Context参数，使用不方便，因此使用上下文设计模式，简化操作。
     *
     * @param args
     */
    public static void main(String[] args) {
        Task task = new Task();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }

}

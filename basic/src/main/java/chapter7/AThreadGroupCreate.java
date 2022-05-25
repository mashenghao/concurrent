package chapter7;

/**
 * 线程组学习：
 * 线程组代表一组线程。 此外，线程组还可以包括其他线程组。 线程组形成一个树，除了初始线程组之外，每个线程组都有一个父进程。
 * 允许线程访问有关其线程组的信息，但不能访问有关其线程组的父线程组或任何其他线程组的信息。
 *
 * @author: mahao
 * @date: 2019/8/14
 */
public class AThreadGroupCreate {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        System.out.println("当前线程组是----" + Thread.currentThread().getThreadGroup());
        //默认使用的构造器线程组是当前线程的线程组，main函数中调用则是main线程组
        ThreadGroup tg1 = new ThreadGroup("tg1");
        System.out.println("t1线程组的父线程组是-----" + tg1.getParent());
        System.out.println("t1线程组是----" + tg1);

    }


}

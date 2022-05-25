package chapter3;

/**
 * 简易API讲解
 *
 * @author: mahao
 * @date: 2019/8/10
 */
public class BSimpleApi {


    public static void main(String[] args) {

        Thread t = new Thread(() -> System.out.println("子线程"));
        t.start();
        System.out.println("线程id---"+t.getId());
        System.out.println("线程优先级---"+t.getPriority());
        System.out.println("线程组---"+t.getThreadGroup());
        System.out.println("线程名字---"+t.getName());

    }









    private long tid;

    public void main2(String[] args) {

        BSimpleApi b = new BSimpleApi();
        b.myMethod();

        System.out.println(b.tid);
    }

    public void myMethod() {
        new Thread(() -> {
            System.out.println("子线程");
            tid = Thread.currentThread().getId();
        }).start();

        while (tid == 0) ;
    }
}

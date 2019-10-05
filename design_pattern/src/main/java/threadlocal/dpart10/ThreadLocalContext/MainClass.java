package threadlocal.dpart10.ThreadLocalContext;


/**
 * 使用上下文设计模式，案例是模式struts2中的ActionContext的设计，简化对线程
 * 使用的变量的参数传递问题。
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class MainClass {


    public static void main(String[] args) {
        Task task = new Task();
        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }

}

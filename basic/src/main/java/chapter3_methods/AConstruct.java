package chapter3_methods;

/**
 *Thread的构造函数细节
 *
 * @author: mahao
 * @date: 2019/8/9
 */
public class AConstruct {

    public static void main(String[] args) {
        /*
            构造函数的参数意义：
            包括： 线程组如何确定
                   线程名命名方式
                   栈深度参数涉及的jvm管理内存结构
                   Runnable开启线程使用的策略设计模式
                   Thread创建线程的模版设计模式

         */
        Thread thread = new Thread(null,()->{
            System.out.println("run 方法运行");
        },"T1",100L);
        thread.start();
    }

}

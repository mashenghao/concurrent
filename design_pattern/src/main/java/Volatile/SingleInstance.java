package Volatile;

import chapter3_methods.ThreadUtil;

/**
 * 双重检查保证单例实体，容易引发npe重现；
 * <p>
 * npe原因：
 * <p>
 * 由于JVM优化，会对不影响结果的指令进行重排序的操作，如果单例对象中，需要初始化多个对象，
 * 这些对象直接没有执行上下的关联关系。则虚拟机可能进行重排序操作， 初始化实例对象可能会早于
 * 某个实例属性，初始化对象完成，但是属性尚未完毕，其他线程访问，实例初始完毕，但是属性为null。
 * 则会引发问题。
 *
 * @author: mahao
 * @date: 2019/8/29
 */
public class SingleInstance {

    private static SingleInstance instance;

    private Object obj1;
    private InitClass initClass;
    private InitClass[] initClasses;


    private SingleInstance() {
        initClasses = new InitClass[10];
        obj1 = new Object();
        initClass = new InitClass();

    }

    public static SingleInstance getInstance() {
        if (instance == null) {
            synchronized (SingleInstance.class) {
                if (instance == null) {
                    instance = new SingleInstance();
                }
            }
        }
        return instance;
    }

    private class InitClass {

        private InitClass() {
            ThreadUtil.sleep(30);
            System.out.println("init InitClass");
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    SingleInstance instance = SingleInstance.getInstance();
                    Object obj1 = instance.obj1;
                    System.out.println(obj1.toString());
                    InitClass initClass = instance.initClass;
                    System.out.println(initClass.toString());
                    instance.initClasses.toString();
                }
            };
            t1.start();
        }

        Thread t2 = new Thread() {
            @Override
            public void run() {
                SingleInstance instance = SingleInstance.getInstance();
                Object obj1 = instance.obj1;
                System.out.println(obj1.toString());
                InitClass initClass = instance.initClass;
                System.out.println(initClass.toString());
                instance.initClasses.toString();
            }
        };

        t2.start();
    }
}

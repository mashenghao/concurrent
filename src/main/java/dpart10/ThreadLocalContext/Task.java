package dpart10.ThreadLocalContext;

import chapter3.ThreadUtil;
import dpart10.Context;

/**
 * 模仿应用，需要使用到上下文中的数据，通过上下文中的数据，去作为程序运行的数据；
 * <p>
 * 程序中需要调用两处服务，去获取程序运行所需要的数据，服务的获取也需要上下文的支持，
 * 通过使用上下文的设计模式，将上下文绑定到了线程中，获取只需要从上下文中获取即可。
 * <p>
 * 问题：为什么需要创建ActionContext类，而且要设计成单例模式？
 * 1.
 * 要创建ActionContext的作用就是获取当前线程的context对象，当然直接使用ThreadLocal对象在
 * 使用的地方创建一个threadlocal对象，然后从中取获取上下文，但是这样不足的是需要在每处使用的地方
 * 都去调用threadLocal.get()方法，需要创建多个threadLocal实例，这是浪费，全局是需要一个就行，所以设计成单例。
 * 2.
 * 是 threadlocal中为创建的线程初始化context的的需要，因为每一个线程中的通过threadlocal存储的数据都是null，只有调用set()方法，
 * 才可以为每个线程中存入数值，否则一直默认的都是null。所以需要类ActionContext，为每个线程中的ThreadLocalMap中存入一个新创建的context
 * 对象，因此可以重写threadLocal中的方法 protected Context initialValue()，这个是在第一次为thread对象的ThreadLocalMap属性创建引用
 * 对象调用的方法，默认返回的是null，即存的是null值，当重写了返回值是return new Context(),则就完成了set操作，以后可以直接使用，
 * 不用考虑未赋值问题。
 * <p>
 * 具体参见ThreadLocal源码
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class Task implements Runnable {

    DBService dbService = new DBService();
    NetService netService = new NetService();

    @Override
    public void run() {
        //从threadLocal中获取到上下文对象
        Context context = ActionContext.getActionContext().getContext();
        context.getSession().put("key", "tid" + ThreadUtil.getName());//设置上下文初始值

        dbService.execute();
        System.out.println("context in name ： " + context.getName());

        String s = netService.execute();
        System.out.println("context in result : " + s);
    }
}

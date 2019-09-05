package dpart10.ThreadLocalContext;

import dpart10.Context;

/**
 * ActionContext是核心类，该类是单例设计，通过该类的对象，能够获取到每个线程的Context对象，
 * 方式是通过Threadlocal存储Context对象。
 *
 * @author: mahao
 * @date: 2019/9/2
 */
/**
 * 使用ThreadLocal设计上下文对象的设计模式
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class ActionContext {


    /**
     * 分析2：
     * <p>
     * 创建一个ThreadLocal实例用于获取每个线程中的threadlocals对象，从而获取里面存的上下文对象。
     * 根据使用，每个线程即使是刚开启，上下文中也应该有一个新创建的context对象，但是根据get()方法的
     * 源码，可以看到，threadlocals中初始化存的值是:
     *
     *      private T setInitialValue() {
     *         T value = initialValue();
     *         Thread t = Thread.currentThread();
     *         ThreadLocalMap map = getMap(t);
     *         if (map != null)
     *             map.set(this, value);
     *         else
     *             createMap(t, value);
     *         return value;
     *     }
     *
     *      protected T initialValue() {
     *         return null;
     *     }
     *  初始值是null，这样在使用上下文对象是会发生NPE错误，因此，需要在为每个线程的threadlocals属性指定ThreadLocalMap
     *  对象时，就应该初始化值成功，因此需要重写initialValue方法，完成初始化。
     *
     * <p/>
     * 问题？
     * 考虑问题，可以看到map.set(this, value)方法，往context对象的key是ThreadLocal对象，如果存在多个
     * ThreadLocal对象，能否获取到同一个value对象，因此去看分析3：
     */
    ThreadLocal<Context> threadLocal = new ThreadLocal<Context>(){

        protected Context initialValue() {
            return new Context();
        }
    };

    /**
     * 分析3：
     *  需要将ActionContext设置为单例设计模式，保证ThreadLocal在应用中只有一个。
     *  因为ThreadLocal的原理是获取到自己存入到thread对象中的ThreadLocalMap对象（理解为map），
     *  然后存值是通过map.put(this,val),map中的key是本对象，如果存在多个threadLocal对象，则会
     *  发生一个线程中存在多个上下文对象，每个threadLocal实例都可以获取到一个context对象，则使用
     *  会混乱。
     */
    private ActionContext() {

    }

    public static ActionContext getActionContext(){
        return ActionContextHolder.instance;
    }

    private static class ActionContextHolder {
        private static final ActionContext instance = new ActionContext();
    }

    /**
     * 分析1：
     * <p>
     * 返回当前线程的上下文context对象，该对象存储在线程thread对象中的threadlocals属性，
     * 需要通过ThreadLocal对象去获取这个属性。
     *
     * @return
     */
    public Context getContext() {
        /*
        去看源码方法具体实现：
        1.获取到当前线程
        2.从当前线程中取出ThreadLocalMap的实例threadlocals，判断null，为null则去创建一个ThreadLocalMap对象，为引用赋值
        3.不为null，则以当前ThreadLocal对象为key，去获取context上下文对象。
         */
        return threadLocal.get();
    }


}

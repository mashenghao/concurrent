package threadlocal.dpart10.demo2;

/**
 * 获取上下文类的帮助类，通过将一个上下文类放在threadlocal中，达到
 * 一个程序一个上下午.需要设置为单例设计模式，要保证多个地方获取的上下文是
 * 同一个。
 *
 * @author: mahao
 * @date: 2019/10/3
 */
public class ActionContext {

    private static final ThreadLocal<Context> threadLocal = new ThreadLocal() {
        protected Context initialValue() {
            return new Context();
        }
    };

    public Context getContext() {
        return threadLocal.get();
    }

    public static ActionContext getActionContext() {
        return Holder.actionContext;
    }

    private static class Holder {
        private static ActionContext actionContext = new ActionContext();
    }

    private ActionContext() {

    }
}

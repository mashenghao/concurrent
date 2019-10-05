package threadlocal.dpart10.demo2;

/**
 * @author: mahao
 * @date: 2019/10/3
 */
public class App extends Thread {

    private Context context;

    public void run() {
        context = ActionContext.getActionContext().getContext();
        System.out.println("程序启动成功，上下文类初始化成功");
    }

    public Context getContext() {
        return context;
    }
}

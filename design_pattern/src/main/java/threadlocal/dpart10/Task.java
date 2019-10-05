package threadlocal.dpart10;

/**
 * 模仿应用，需要使用到上下文中的数据，通过上下文中的数据，去作为程序运行的数据；
 * <p>
 * 程序中需要调用两处服务，去获取程序运行所需要的数据，服务的获取也需要上下文的支持，所以
 * 需要把上下文作为参数，传入到程序中。
 *
 * @author: mahao
 * @date: 2019/9/2
 */
public class Task implements Runnable {

    DBService dbService = new DBService();
    NetService netService = new NetService();

    @Override
    public void run() {
        //为线程创建一个上下文
        final Context context = new Context();
        context.getSession().put("key", "uid123812888");//设置上下文初始值

        dbService.execute(context);
        System.out.println("context in name ： " + context.getName());

        String s = netService.execute(context);
        System.out.println("context in result : " + s);
    }
}

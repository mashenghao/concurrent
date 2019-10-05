package channel;

import chapter3.ThreadUtil;

/**
 * 请求任务
 *
 * @author: mahao
 * @date: 2019/9/4
 */
public class Request {

    private final String name; //  委托者
    private final int number;  // 请求编号

    public Request(String name, int i) {
        number = i;
        this.name = name;
    }

    /**
     * 由工人需要执行的任务方法
     */
    public void execute() {

        ThreadUtil.sleep(1000);
        System.out.println(Thread.currentThread().getName() + " executes " + this);
    }

    public String toString() {
        return "[ Request from " + name + " No." + number + " ]";
    }
}

package channel;

/**
 * 工人线程，负责一直处理传输带上的产品
 * //工人线程的任务就是，不停的从channel去任务，然后执行任务
 *
 * @author: mahao
 * @date: 2019/9/4
 */
public class WorkThread extends Thread {

    private Channel channel;

    public WorkThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        //工人线程的任务就是，不停的从channel去任务，然后执行任务
        while (true) {
            Request request = channel.take();
            request.execute();
        }
    }
}

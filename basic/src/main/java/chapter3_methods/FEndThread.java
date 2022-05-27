package chapter3_methods;

/**
 * @author: mahao
 * @date: 2019/8/11
 */
public class FEndThread {

    public static void main(String[] args) {
        ThreadService threadService = new ThreadService();

        threadService.execute(() -> {
            try {
                System.out.println("正在执行任务");
                Thread.sleep(30 * 1000);
                System.out.println("任务执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("ssss");
        threadService.shutDown(3 * 1000);
    }
}

/**
 * 将要执行的线程设置为守护线程，结束任务，只需要结束创建线程即可。
 */
class ThreadService {

    private volatile boolean finished = false;

    private Thread executeThread;

    //执行线程
    public void execute(Runnable task) {

        executeThread = new Thread() { // 需要在方法里面另开一个线程去执行线程任务，因为需要设定结束时间
            @Override
            public void run() {

                Thread runner = new Thread(task);
                runner.setDaemon(true);
                runner.start();

                try {
                    //主线程等待守护线程运行结合，调用join方法的是executeThread线程
                    runner.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finished = true;
                System.out.println("...  执行结束  ...");
            }
        };

        executeThread.start();//执行线程
    }

    //关闭线程
    public void shutDown(long mills) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {//当任务尚未结束

            //线程执行时间已经大于了要求时间
            if (System.currentTimeMillis() - currentTime > mills) {
                System.out.println("任务超时，结束了线程任务");
                executeThread.interrupt(); //给执行线程设置中断标记
                break;//停止循环时间等待
            }
        }
        System.out.println("任务提前完成");

    }
}

package TwoPhaseTermination;

/**
 * 创建一个计数器，可以循环使用，当一个线程使用完毕，
 * 或者异常结束，需要将技术重置初始值。
 *
 * @author: mahao
 * @date: 2019/9/3
 */
public class CounterTermination extends Thread {

    private int i;

    private volatile boolean termination = false;

    @Override
    public void run() {
        try {
            while (!termination) {
                i++;
                Thread.sleep(1000);
                System.out.println("CounterTermination.run # the i :" + i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //结束负责清理工作
            this.clean();
        }

    }

    private void clean() {
        i = 0;
        System.out.println("CounterTermination.clean # cleaning the program");
    }

    public void shutdown() {
        termination = true;
        this.interrupt();
    }
}

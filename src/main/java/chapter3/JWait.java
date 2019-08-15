package chapter3;

/**
 * Object 中wait和notify方法的使用：
 * wait是使当前线程陷入等待，知道被其他线程唤醒。该方法必须加到同步代码块中，也就是先获取
 * 到对象的monitor
 *
 * @author: mahao
 * @date: 2019/8/13
 */
public class JWait {

    public static void main(String[] args) throws InterruptedException {
        JWait jWait = new JWait();
        String lock = "";
        ThreadUtil.execute(() -> {//3秒后唤醒线程
            ThreadUtil.sleep(3000); // wait要放在同步代码块外面，因为当该线程获得
            //锁后，sleep不释放，会先唤醒，这时候wait将不会唤醒。   也可采用添加标记，当唤醒调用后，就不让线程wait了
            synchronized (lock) {
                lock.notifyAll();
            }

        });
        ThreadUtil.execute(() -> {
            synchronized (lock) {//java.lang.IllegalMonitorStateException
                try {
                    System.out.println("wait等待---");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("执行完毕");
            }

        });

    }
}

package dpart14;

import chapter3.ThreadUtil;

/**
 * Two Phase Termination(两阶段终止)设计模式，是为了线程
 * 结束后的清理工作。
 *
 * @author: mahao
 * @date: 2019/9/3
 */
public class Mainclass {


    public static void main(String[] args) {
        CounterTermination counterTermination = new CounterTermination();
        counterTermination.start();

        //运行5s后，结束程序
        ThreadUtil.sleep(5000);
        counterTermination.shutdown();
    }
}

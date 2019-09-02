package dpart3.observer;



/**
 * @program: concurrent
 * @Date: 2019/8/26 19:34
 * @Author: mahao
 * @Description:
 */
public interface Observer {

    /**
     * 被监听者状态发生改变，调用的方法
     */
    void handler(Observable observable, Object arg);
}

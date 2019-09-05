package dpart3.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听者抽象父类
 *
 * @author: mahao
 * @date: 2019/8/26
 */
public abstract class Observable {

    protected List<Observer> observers;

    public Observable() {
        observers = new ArrayList<>();
    }


    public abstract void doChange();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public abstract void notifyAllChange();

}

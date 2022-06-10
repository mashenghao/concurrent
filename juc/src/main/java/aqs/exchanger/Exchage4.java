package aqs.exchanger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * @author: mahao
 * @date: 2019/9/23
 */
class ThreadLocalTest {

    public static void main(String[] args) {
        Exchanger<List<Integer>> exchanger = new Exchanger<List<Integer>>();
        new Consumer(exchanger).start();
        new Producer(exchanger).start();
    }

}

class Producer extends Thread {
    List<Integer> list = new ArrayList<Integer>();
    Exchanger<List<Integer>> exchanger = null;

    public Producer(Exchanger<List<Integer>> exchanger) {
        super();
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            list.clear();
            list.add(rand.nextInt(10000));
            list.add(rand.nextInt(10000));
            list.add(rand.nextInt(10000));
            list.add(rand.nextInt(10000));
            list.add(rand.nextInt(10000));
            try {
                list = exchanger.exchange(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {
    List<Integer> list = new ArrayList<Integer>();
    Exchanger<List<Integer>> exchanger = null;

    public Consumer(Exchanger<List<Integer>> exchanger) {
        super();
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                list = exchanger.exchange(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(list.get(0) + ", ");
            System.out.print(list.get(1) + ", ");
            System.out.print(list.get(2) + ", ");
            System.out.print(list.get(3) + ", ");
            System.out.println(list.get(4) + ", ");
        }
    }
}

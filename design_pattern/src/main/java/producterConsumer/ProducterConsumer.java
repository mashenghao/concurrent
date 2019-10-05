package producterConsumer;

import java.util.LinkedList;

/**
 * @author: mahao
 * @date: 2019/9/3
 */
public class ProducterConsumer {

    public static void main(String[] args) {
        LinkedList<String> queue = new LinkedList<>();
        new Producter(queue, 5).start();
        new Producter(queue, 5).start();
        new Producter(queue, 5).start();
        new Consumer(queue).start();
    }
}

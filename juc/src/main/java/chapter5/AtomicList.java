package chapter5;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 如果想要使Node的操作具有原子性，可以使用AtomicReference，来操作对象.
 * 尚未完成。
 * TODO： 尚未实现;
 *
 * @author: mahao
 * @date: 2019/9/22
 */
public class AtomicList {

    AtomicReference<Node> referHead = new AtomicReference<Node>();


    public void add(String val) {

    }


}


class Node {

    Node prev;
    String val;
    Node next;

    public Node(Node prev, String val, Node next) {
        this.prev = prev;
        this.val = val;
        this.next = next;
    }

    public Node(String val) {
        this(null, val, null);
    }
}

class AtomicNode {

    AtomicNode prev;
    String val;
    AtomicNode next;
    AtomicReference<Node> atomicReference = new AtomicReference<Node>();

    public AtomicNode(AtomicNode prev, String val, AtomicNode next) {
        this.prev = prev;
        this.val = val;
        this.next = next;
    }

    public void setPrev(AtomicNode prev) {
        this.prev = prev;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public void setNext(AtomicNode next) {
        this.next = next;
    }
}
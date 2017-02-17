import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    public int key;
    public Node next;
    Lock nodelock;
    Node (int item){
        key = item;
        next=null;
        nodelock = new ReentrantLock();
    }

}

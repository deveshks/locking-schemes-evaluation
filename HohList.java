import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class HohList implements  IntSet{
    private Node head;
    private final Lock lock = new ReentrantLock();
    public HohList()
    {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    public boolean insert(int item) {
        head.nodelock.lock();
        Node pred = head;
        Node curr = head.next;
        try {
            curr.nodelock.lock();
            try {
                while (curr.key < item){
                    pred.nodelock.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.nodelock.lock();
                }
                if (curr.key == item)
                    return false;
                Node node = new Node(item);
                node.next = curr;
                pred.next = node;
                return true;
            } finally{
                curr.nodelock.unlock();
            }
        } finally{
            pred.nodelock.unlock();
        }
    }

    public boolean contain(int item) {
        head.nodelock.lock();
        Node pred = head;
        Node curr = pred.next;
        try {
            curr.nodelock.lock();
            try {
                while (curr.key < item){
                    pred.nodelock.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.nodelock.lock();
                }
                return (curr.key == item);
            } finally{
                curr.nodelock.unlock();
            }
        } finally{
            pred.nodelock.unlock();
        }
    }
    public boolean remove(int item){
        head.nodelock.lock();
        Node pred = head;
        Node curr = pred.next;
        try {
            curr.nodelock.lock();
            try {
                while (curr.key < item){
                    pred.nodelock.unlock();
                    pred = curr;
                    curr = pred.next;
                    curr.nodelock.lock();
                }
                if (curr.key == item){
                    pred.next = curr.next;
                    return true;
                }
                return false;
            } finally{
                curr.nodelock.unlock();
            }
        } finally{
            pred.nodelock.unlock();
        }
    }
}

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SetList implements  IntSet{
    private Node head;
    private Lock lock = new ReentrantLock();
    public SetList()
    {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }
    public boolean insert(int item){
        lock.lock();
        Node pred = head;
        Node curr = head.next;
        while (curr.key < item){
            pred = curr;
            curr = pred.next;
        }
        if (curr.key == item) {
            lock.unlock();
            return false;
        }
        else{
            Node node = new Node(item);
            node.next = curr;
            pred.next = node;
            lock.unlock();
            return true;
        }

    }
    public boolean contain(int item){
        lock.lock();
        Node pred = head;
        Node cur = head.next;
        while(cur.key < item){
            pred = cur;
            cur = cur.next;
        }
        int val = cur.key;
        lock.unlock();
        return (val == item);
    }
    public boolean remove(int item){
        lock.lock();
        Node pred = head;
        Node cur = head.next;
        while(cur.key <item)
        {
            pred = cur;
            cur = cur.next;
        }
        if(cur.key == item)
        {
            pred.next = cur.next;
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;
    }
    public void printList()
    {
        Node cur = head.next;
        while(cur.key != Integer.MAX_VALUE)
        {
            System.out.println(cur.key);
            cur = cur.next;
        }
    }
}

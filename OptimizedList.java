import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OptimizedList implements IntSet{
    private Node head;
    private final Lock lock = new ReentrantLock();
    public OptimizedList()
    {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }
    private boolean validate(Node pred, Node cur)
    {
        Node node = head;
        while(node.key <= pred.key)
        {
            if(node == pred){
                return (pred.next == cur);
            }
            node = node.next;
        }
        return false;
    }
    public boolean insert(int item){
        while(true)
        {
            Node pred = head;
            Node cur = head.next;
            while(cur.key < item){
                pred = cur;
                cur=cur.next;
            }
            pred.nodelock.lock();
            cur.nodelock.lock();
            try{
                if(validate(pred, cur)){
                    if(cur.key == item){
                        return false;
                    }
                    Node node= new Node(item);
                    node.next = cur;
                    pred.next = node;
                    return true;
                }
            }finally {
                pred.nodelock.unlock();
                cur.nodelock.unlock();
            }
        }
    }
    public boolean contain(int item){
       while(true)
       {
           Node pred = head;
           Node cur = head.next;
           while(cur.key <item)
           {
               pred = cur;
               cur = cur.next;
           }
           pred.nodelock.lock();
           cur.nodelock.lock();
           try{
               if (validate(pred, cur)) {
                   return (cur.key == item);
               }
           }finally {
               pred.nodelock.unlock();
               cur.nodelock.unlock();
           }
       }
    }
    public boolean remove(int item){
        while(true){
            Node pred = head;
            Node cur = head.next;
            while(cur.key < item)
            {
                pred = cur;
                cur = cur.next;
            }
            pred.nodelock.lock();
            cur.nodelock.lock();
            try{
                if (validate(pred, cur)) {
                    if (cur.key == item)
                    {
                        pred.next = cur.next;
                        return true;
                    }
                    return false;
                }
            }finally {
                pred.nodelock.unlock();
                cur.nodelock.unlock();
            }
        }
    }
}

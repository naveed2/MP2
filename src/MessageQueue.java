import java.util.LinkedList;
import java.util.List;

public class MessageQueue {

    private List<String> msgQueue;

    public MessageQueue() {
        msgQueue = new LinkedList<String>();
    }

    public void add(String msg) {
        synchronized (this) {
            msgQueue.add(msg);
        }
    }

    public String top() {
        synchronized (this) {
            return msgQueue.get(0);
        }
    }

    public synchronized String removeTop() {
        synchronized (this) {
            String ret = msgQueue.get(0);
            msgQueue.remove(0);
            return ret;
        }
    }
}

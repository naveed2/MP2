package Message;

import java.util.LinkedList;
import java.util.List;

public class MessageQueue {

    private List<String> msgQueue;

    private final List<MessageReceivedListener> messageReceivedListeners;

    public MessageQueue() {
        msgQueue = new LinkedList<String>();
        messageReceivedListeners = new LinkedList<MessageReceivedListener>();
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

    public MessageQueue addMessageReceivedListener(MessageReceivedListener mrl) {
        synchronized (messageReceivedListeners) {
            messageReceivedListeners.add(mrl);
        }
        return this;
    }

    public void fireMessageReceiving() {
        String stringMsg = top();
        Message msg = Message.generateMessageFromString(stringMsg);

        synchronized (messageReceivedListeners) {
            for(MessageReceivedListener mrl : messageReceivedListeners) {
                mrl.onReceivingMessage(msg);
            }
        }
    }
}

package Transmission;

import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.LinkedList;
import java.util.List;

public class EventMessageQueue {

    private List<EventMessage> eventQueue;

    private final List<MessageReceivedListener> messageReceivedListeners;

    private Logger logger = Logger.getLogger(EventMessageQueue.class);

    public EventMessageQueue() {
        eventQueue = new LinkedList<EventMessage>();
        messageReceivedListeners = new LinkedList<MessageReceivedListener>();
    }


    public void add(EventMessage msg) {
        synchronized (this) {
            eventQueue.add(msg);
            fireMessageReceiving();
        }
    }

    public EventMessage top() {
        synchronized (this) {
            return eventQueue.get(0);
        }
    }

    public synchronized EventMessage removeTop() {
        synchronized (this) {
            EventMessage ret = eventQueue.get(0);
            eventQueue.remove(0);
            return ret;
        }
    }

    public EventMessageQueue addMessageReceivedListener(MessageReceivedListener mrl) {
        synchronized (messageReceivedListeners) {
            messageReceivedListeners.add(mrl);
        }
        return this;
    }

    public void fireMessageReceiving() {
        EventMessage em = top();
        removeTop();

        synchronized (messageReceivedListeners) {
            for(MessageReceivedListener mrl: messageReceivedListeners) {
                mrl.onReceivingMessage(em);
            }
        }

    }
}

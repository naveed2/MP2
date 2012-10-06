package Server;

import Main.DistributedMachine;
import Transmission.EventMessage;
import Transmission.EventMessageQueue;
import Transmission.Message;
import Transmission.MessageReceivedListener;
import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.UUID;

/**
 * Used by contact machine. This is UDP server.
 */
public class UDPServer {

    private DatagramSocket serverSocket;
    private int port;
    private static final Integer BUFFER_SIZE = 1024;
    private EventMessageQueue eventMessageQueue;

    private Logger logger = Logger.getLogger(UDPServer.class);

    public UDPServer(int port) {
        this.port = port;
        initMessageQueue();
    }

    /**
     * Add a MessageReceivedListener.
     */
    private void initMessageQueue() {
        eventMessageQueue = new EventMessageQueue();
        eventMessageQueue.addMessageReceivedListener(new MessageReceivedListener() {
            public void onReceivingMessage(EventMessage m) {
                DistributedMachine.getTimestamp().incrementAndGet();
                if(m.getEventType() == EventMessage.EventType.Join) {
                    joinMachine(m);
                } else if (m.getEventType() == EventMessage.EventType.Leave) {
                    leaveMachine(m);
                }
            }
        });
    }

    private void joinMachine(EventMessage m) {
        MachineInfo mi = m.getMachineInfo();
        DistributedMachine.addMachine(mi);
    }

    private void leaveMachine(EventMessage m) {
        MachineInfo mi = m.getMachineInfo();
        DistributedMachine.removeMachine(mi);
    }

    public EventMessageQueue getEventMessageQueue() {
        return eventMessageQueue;
    }

    /**
     * Start UDP server. When receiving a message, it extracts the information from it and encapsulate them into
     * an EventMessage object.
     * @throws SocketException
     */
    public void start() throws SocketException {
        serverSocket = new DatagramSocket(port);
        new Thread(new Runnable() {
            public void run() {

                byte[] receiveBuffer = new byte[BUFFER_SIZE];

                try{
                    while(true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,BUFFER_SIZE);
                        serverSocket.receive(receivePacket);
                        String receiveString = new String(receivePacket.getData());

                        UUID uuid = Message.getUUIDFromMessageString(receiveString);
                        int timestamp = Message.getTimestampFromMessageString(receiveString);
                        int type = Message.getTypeFromMessageString(receiveString);
                        int port = Message.getPortFromMessageString(receiveString);

                        SocketAddress sa = receivePacket.getSocketAddress();
                        String[] address = sa.toString().split(":");
                        if(address[0].startsWith("/")) {
                            address[0] = address[0].substring(1);
                        }

                        MachineInfo machineInfo = new MachineInfo(address[0], port);
                        machineInfo.setUUID(uuid).setTimestamp(timestamp);

                        EventMessage em = null;
                        System.out.println("type = " + type);
                        if(type == 0 ) {
                            em = new EventMessage(EventMessage.EventType.Join);
                            em.setMachineInfo(machineInfo);
                            logger.info("Receive an join message");
                        } else if(type == 1) {
                            em = new EventMessage(EventMessage.EventType.Leave);
                            em.setMachineInfo(machineInfo);
                            logger.info("Receive an leave message");
                        } else if (type == -1){
                            logger.error("Receive an unknown type message");
                        }

                        if(em != null) {
                            eventMessageQueue.add(em);
                        }


                        logger.info("Receive " + receiveString);
//                        System.out.println();
//                        System.out.println(receiveString);
//                        System.out.print(">");
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex.toString());
                }
            }
        }).start();
    }
}

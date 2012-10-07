package Server;

import Main.DistributedMachine;
import Transmission.EventMessage;
import Transmission.EventMessageQueue;
import Transmission.Message;
import Main.MemberList;
import Transmission.MessageReceivedListener;
import java.io.IOException;
import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Used by contact machine. This is UDP server.
 */
public class UDPServer {

    private DatagramSocket serverSocket;
    private int port;
    private static final Integer BUFFER_SIZE = 1024;
    private EventMessageQueue eventMessageQueue;

    private Thread serverThread;

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
           
            public void onReceivingMessage(EventMessage m) throws ParserConfigurationException, TransformerException, UnknownHostException, IOException {
                DistributedMachine.getTimestamp().incrementAndGet();
                if(m.getEventType() == EventMessage.EventType.Join) {
                    joinMachine(m);
                    DistributedMachine.sync();
                } else if (m.getEventType() == EventMessage.EventType.Leave) {
                    leaveMachine(m);
                    DistributedMachine.sync();
                } else if (m.getEventType() == EventMessage.EventType.Sync){
                    syncMachine(m);
                } else if (m.getEventType() == EventMessage.EventType.Ping) {
                    pingAckMachine(m);
                } else if (m.getEventType() == EventMessage.EventType.Ping_ACK) {
                    receivePingAckFromMachine(m);
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
        DistributedMachine.leaveMachine(mi);
    }
    
    private void syncMachine(EventMessage m){
        MemberList list = m.getMemberList();
        list.add(m.getMachineInfo());
        for(MachineInfo mi : list.getAll()) {
            DistributedMachine.syncMachine(mi);
        }
//        DistributedMachine.syncMachine(m);
    }

    private void pingAckMachine(EventMessage m) {
        try{
            DistributedMachine.pingAck(m.getMachineInfo());
        } catch(Exception ex) {
            ex.printStackTrace();
            logger.info(ex);
        }
    }

    private void receivePingAckFromMachine(EventMessage m) {
        MachineInfo mi = m.getMachineInfo();
        for(MachineInfo tmp : DistributedMachine.getMemberList().getAll()) {
            if(tmp.getUUID().equals(mi.getUUID())) {
                tmp.receivePingAck();
                return;
            }
        }
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
        serverThread = new Thread(new Runnable() {
            public void run() {



                try{
                    while(true) {
                        byte[] receiveBuffer = new byte[BUFFER_SIZE];

                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,BUFFER_SIZE);
                        serverSocket.receive(receivePacket);
                        String receiveString = new String(receivePacket.getData());
                        receiveString = trim(receiveString);


                        UUID uuid = Message.getUUIDFromMessageString(receiveString);
                        int timestamp = Message.getTimestampFromMessageString(receiveString);
                        int type = Message.getTypeFromMessageString(receiveString);
                        int port = Message.getPortFromMessageString(receiveString);
                        MachineInfo.MachineState state = Message.getStateFromMessageString(receiveString);


                        

                        SocketAddress sa = receivePacket.getSocketAddress();
                        String[] address = sa.toString().split(":");
                        if(address[0].startsWith("/")) {
                            address[0] = address[0].substring(1);
                        }

                        MachineInfo machineInfo = new MachineInfo(address[0], port);
                        machineInfo.setUUID(uuid).setTimestamp(timestamp).setState(state);

                        EventMessage em = null;
                        
                        if(type == 0 ) {    // join message
                            machineInfo.setStateConnected();
                            em = new EventMessage(EventMessage.EventType.Join);
                            em.setMachineInfo(machineInfo);
                            logger.info("Received a join message");
                        } else if(type == 1) { //leave message
                            em = new EventMessage(EventMessage.EventType.Leave);
                            em.setMachineInfo(machineInfo);
                            logger.info("Received a leave message");
                        } else if(type == 2) { //sync message
                            em = new EventMessage(EventMessage.EventType.Sync);
                            em.setMemberList(Message.getMemberListfromMessageString(receiveString));
                            em.setMachineInfo(machineInfo);
                            logger.info("Received a sync message");
                        } else if(type == 3) { //ping message
                            em = new EventMessage(EventMessage.EventType.Ping);
                            em.setMachineInfo(machineInfo);
                            logger.info("Received a ping");
                        } else if(type == 4) { //receive pingack
                            em = new EventMessage(EventMessage.EventType.Ping_ACK);
                            em.setMachineInfo(machineInfo);
                            logger.info("Received a pingack");
                        }
                        
                        else if (type == -1){
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
        });

        serverThread.start();
    }

    private String trim(String receiveString) {
        String str = receiveString.substring(0, receiveString.lastIndexOf(">")+1);
        return str;
    }

    public void close() {
        serverThread.stop();
    }

    public static void main(String[] args) {
        String str = "012663456";
        System.out.println(str.lastIndexOf("6"));
        System.out.println(str.substring(0,8));
    }
}

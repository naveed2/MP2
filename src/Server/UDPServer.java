package Server;

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

    private void initMessageQueue() {
        eventMessageQueue = new EventMessageQueue();
        eventMessageQueue.addMessageReceivedListener(new MessageReceivedListener() {
            public void onReceivingMessage(EventMessage m) {
                //TODO
            }
        });
    }

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

                        SocketAddress sa = receivePacket.getSocketAddress();
                        String[] address = sa.toString().split(":");

                        MachineInfo machineInfo = new MachineInfo(address[0], Integer.parseInt(address[1]));
                        machineInfo.setUUID(uuid).setTimestamp(timestamp);

                        EventMessage em = null;
                        if(type == 0 ) {
                            em = new EventMessage(EventMessage.EventType.Join);
                            em.setMachineInfo(machineInfo);

                        } else if (type == -1){
                            logger.error("Receive an unknown type message");
                        }

                        if(em != null) {
                            eventMessageQueue.add(em);
                        }


                        //TODO:for test
                        System.out.println();
                        System.out.println(receiveString);
                        System.out.print(">");
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex.toString());
                }
            }
        }).start();
    }
}

import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

/**
 * Used by contact machine. This is UDP server.
 */
public class UDPServer {

    private DatagramSocket serverSocket;
    private int port;
    private static final Integer BUFFER_SIZE = 1024;
    private MessageQueue msgQueue;

    private Logger logger = Logger.getLogger(UDPServer.class);

    public UDPServer(int port) {
        this.port = port;
        this.msgQueue = new MessageQueue();
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
                        msgQueue.add(receiveString);

                        //TODO:dirty code nowx

                        //TODO:for test
                        System.out.println();
                        System.out.println(receiveString);
                        System.out.print(">");
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex.getMessage());
                }
            }
        }).start();
    }
}

import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Used by contact machine. This is UDP server.
 */
public class UDPServer {

    private DatagramSocket serverSocket;
    private int port;
    private static final Integer BUFFER_SIZE = 512;

    private Logger logger = Logger.getLogger(UDPServer.class);

    public UDPServer(int port) {
        this.port = port;
    }

    public void start() throws SocketException {
        serverSocket = new DatagramSocket(port);
        new Thread(new Runnable() {
            public void run() {

                byte[] receiveBuffer = new byte[BUFFER_SIZE];
                byte[] sendBuffer = new byte[BUFFER_SIZE];

                try{
                    while(true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,BUFFER_SIZE);
                        serverSocket.receive(receivePacket);
                        String test = new String(receivePacket.getData());
                        System.out.println(test);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex.getMessage());
                }
            }
        }).start();
    }
}

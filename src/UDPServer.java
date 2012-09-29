import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Used by contact machine. This is UDP server.
 */
public class UDPServer {

    private DatagramSocket serverSocket;
    private int port;

    public UDPServer(int port) {
        this.port = port;
    }

    public void start() throws SocketException {
        serverSocket = new DatagramSocket(port);
    }
}

import Transmission.Message;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class msg {
	
	public static void main(String[] args) throws UnknownHostException, ParserConfigurationException, TransformerException{
		
		InetAddress address = InetAddress.getLocalHost();

		Message msg = Message.generateJoinMessage(address, UUID.randomUUID(), 0);

        ByteOutputStream bos = new ByteOutputStream();

		msg.toxmlString(bos);
        msg.toxmlString(System.out);

        String str = new String(bos.getBytes());
        System.out.println(str);

        msg = Message.generateMessageFromString(str);
        msg.toxmlString(System.out);
	}



}

import Message.Message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class msg {
	
	public static void main(String[] args) throws UnknownHostException, ParserConfigurationException, TransformerException{
		
		InetAddress address = InetAddress.getLocalHost();

		Message msg = Message.generateJoinMessage(address, UUID.randomUUID(), 0);
		
		msg.toxmlString(System.out);
	}

}

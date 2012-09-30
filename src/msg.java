import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class msg {
	
	public static void main(String[] args) throws UnknownHostException, ParserConfigurationException, TransformerException{
		
		InetAddress address = InetAddress.getLocalHost();
		Timestamp timestamp = Timestamp.valueOf("2012-09-29 12:12:12.22");
		Message msg =Message.generateJoinMessage(address, 1, timestamp);
		
		msg.toxmlString();
	}

}

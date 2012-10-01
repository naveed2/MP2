package Message;

import java.io.File;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class Message {
	
	//Message types, we have four types of message 
	//join message i.e. 0, leave message i.e. 1, sync message i.e. 2, ping message i.e. 3 and ping ack message i.e. 4
	
	int _messageType;
	public InetAddress _IP = null;
	public UUID _id;
	public int _time_stamp;
	
	public Message(){
		
	}
	
	public static Message generateJoinMessage(InetAddress IP_join, UUID id_join, int time_stamp_join){
		Message msg = new Message();
		msg._messageType = 0;
		msg._IP = IP_join;
		msg._id = id_join;
		msg._time_stamp = time_stamp_join;
		return msg;
	}

	public Message generateLeaveMessage(InetAddress IP_leave, UUID id_join, int time_stamp_join){
		Message msg = new Message();
		msg._messageType = 1;
		msg._IP = IP_leave;
		msg._id = id_join;
		msg._time_stamp = time_stamp_join;
		return msg;
	}

    public static Message generateMessageFromString(String xmlStr) {
        return null;
    }

	public void toxmlString(OutputStream os) throws ParserConfigurationException, TransformerException{
		//return Integer.toString(messageType) + IP.toString() + Integer.toString()

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		Document doc = docBuilder.newDocument();
		
		Element root = doc.createElement("message");
		doc.appendChild(root);
		
		
		
		
		Element messageType = doc.createElement("messagetype");
		messageType.appendChild(doc.createTextNode(Integer.toString(_messageType)));
		root.appendChild(messageType);		
		
		Element IP = doc.createElement("ip");
		IP.appendChild(doc.createTextNode(_IP.toString()));
		root.appendChild(IP);
		
		Element id = doc.createElement("id");
		id.appendChild(doc.createTextNode(_id.toString()));
		root.appendChild(id);
		
		Element time_stamp = doc.createElement("timestamp");
		time_stamp.appendChild(doc.createTextNode(Integer.toString(_time_stamp)));
		root.appendChild(time_stamp);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("message.xml"));
		
		StreamResult result1 = new StreamResult(os);
		
		transformer.transform(source, result);
		transformer.transform(source, result1);

        //TODO:what does "file saved" mean?
		System.out.println("File saved!");
 
		
		
	}
	
	

}

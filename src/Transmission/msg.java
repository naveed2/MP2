package Transmission;

import Transmission.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class msg {
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException{
		
		InetAddress address = InetAddress.getLocalHost();

		Message msg = Message.generateJoinMessage(address, UUID.randomUUID(), 0);
        msg.setServerPort(100);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

//		msg.toxmlString(bos);
//        msg.toxmlString(System.out);
//        bos.close();
//
//        String str = new String(bos.toByteArray());
//        System.out.println(str);
//
//        msg = Message.generateMessageFromString(str);
//        msg.toxmlString(System.out);
	}



}

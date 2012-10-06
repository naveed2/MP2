package Transmission;

import java.io.*;
import java.net.InetAddress;
import java.util.UUID;
import Main.MemberList;
import Server.MachineInfo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Util.UtilityTool;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Message {

    //Message types, we have four types of message 
    //join message i.e. 0, leave message i.e. 1, sync message i.e. 2, ping message i.e. 3 and ping ack message i.e. 4
    int _messageType; //TODO: need to be refactored, using join/leave
    private InetAddress _IP = null;
    private UUID _id;
    private int _time_stamp;
    private int serverPort;
    private static Logger logger = Logger.getLogger(Message.class);
    private static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

    public Message() {
    }

    public static Message generateJoinMessage(InetAddress IP_join, UUID id_join, int time_stamp_join) {
        Message msg = new Message();
        msg._messageType = 0;
        msg._IP = IP_join;
        msg._id = id_join;
        msg._time_stamp = time_stamp_join;
        return msg;
    }

    public Message setIP(InetAddress ip) {
        this._IP = ip;
        return this;
    }

    public Message setUUID(UUID uuid) {
        this._id = uuid;
        return this;
    }

    public Message setTimeStamp(int timeStamp) {
        this._time_stamp = timeStamp;
        return this;
    }

    public static Message generateLeaveMessage(UUID id_join, int time_stamp_join) {
        Message msg = new Message();
        msg._messageType = 1;
        msg._IP = null;
        msg._id = id_join;
        msg._time_stamp = time_stamp_join;
        return msg;
    }

    public static Message generateSyncMessage(MachineInfo mi) throws UnknownHostException {

        Message msg = new Message();
        msg._messageType = 2;
        msg._IP = InetAddress.getByName(mi.getAddress());
        msg._id = mi.getUUID();
        msg._time_stamp = mi.getTimestamp();
        return msg;

    }

    public static Message generateMessageFromString(String xmlStr) throws ParserConfigurationException, TransformerException {

        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        try {

            Document doc = docBuilder.parse(new ByteArrayInputStream(xmlStr.getBytes()));

            String ip, id, timestamp;
            ip = doc.getElementsByTagName("ip").item(0).getFirstChild().getNodeValue();
            id = doc.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
            timestamp = doc.getElementsByTagName("timestamp").item(0).getFirstChild().getNodeValue();


            Message msg = new Message();
            msg.setIP(UtilityTool.getInetAddress(ip))
                    .setUUID(UUID.fromString(id))
                    .setTimeStamp(Integer.parseInt(timestamp));

            return msg;

        } catch (SAXException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }

    public static int getTypeFromMessageString(String xmlStr) {
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(xmlStr.trim())));

            String type = doc.getElementsByTagName("messagetype").item(0).getFirstChild().getNodeValue();

            return Integer.parseInt(type);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            logger.error(e.toString());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return -1;
    }

    public static UUID getUUIDFromMessageString(String xmlStr) {
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(new InputSource(new StringReader(xmlStr.trim())));

            String uuid = doc.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();

            return UUID.fromString(uuid);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            logger.error(e.toString());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return null;
    }

    public static Integer getTimestampFromMessageString(String xmlStr) {
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(new InputSource(new StringReader(xmlStr.trim())));

            String timestamp = doc.getElementsByTagName("timestamp").item(0).getFirstChild().getNodeValue();

            return Integer.parseInt(timestamp);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            logger.error(e.toString());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return null;
    }

    public static Integer getPortFromMessageString(String xmlStr) {
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(new InputSource(new StringReader(xmlStr.trim())));

            String port = doc.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();

            return Integer.parseInt(port);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            logger.error(e.toString());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return null;
    }

    public void toxmlSyncString(OutputStream os, MemberList list) throws ParserConfigurationException, TransformerException, UnknownHostException {
        //return Integer.toString(messageType) + IP.toString() + Integer.toString()

        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        Element root = doc.createElement("message");
        doc.appendChild(root);

        for (int i = 0; i < list.size(); i++) {

            Message msg = generateSyncMessage(list.get(i));

            Element machine = doc.createElement("machine");
            root.appendChild(machine);


            Element messageType = doc.createElement("messagetype");
            messageType.appendChild(doc.createTextNode(Integer.toString(msg._messageType)));
            machine.appendChild(messageType);

            Element IP = doc.createElement("ip");
            if (_IP != null) {
                IP.appendChild(doc.createTextNode(msg._IP.getHostAddress()));
            } else {
                IP.appendChild(doc.createTextNode(""));
            }
            machine.appendChild(IP);

            Element port = doc.createElement("port");
            port.appendChild(doc.createTextNode(String.valueOf(msg.serverPort)));
            machine.appendChild(port);

            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(_id.toString()));
            machine.appendChild(id);

            Element time_stamp = doc.createElement("timestamp");
            time_stamp.appendChild(doc.createTextNode(Integer.toString(msg._time_stamp)));
            machine.appendChild(time_stamp);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("syncmessage.xml"));

        StreamResult result1 = new StreamResult(os);

        transformer.transform(source, result);
        transformer.transform(source, result1);

        //TODO:why we need to save a file?
        System.out.println("File saved!");



    }

    public void toxmlString(OutputStream os) throws ParserConfigurationException, TransformerException {
        //return Integer.toString(messageType) + IP.toString() + Integer.toString()

        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        Element root = doc.createElement("message");
        doc.appendChild(root);




        Element messageType = doc.createElement("messagetype");
        messageType.appendChild(doc.createTextNode(Integer.toString(_messageType)));
        root.appendChild(messageType);

        Element IP = doc.createElement("ip");
        if (_IP != null) {
            IP.appendChild(doc.createTextNode(_IP.getHostAddress()));
        } else {
            IP.appendChild(doc.createTextNode(""));
        }
        root.appendChild(IP);

        Element port = doc.createElement("port");
        port.appendChild(doc.createTextNode(String.valueOf(serverPort)));
        root.appendChild(port);

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

        //TODO:why we need to save a file?
        System.out.println("File saved!");



    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}

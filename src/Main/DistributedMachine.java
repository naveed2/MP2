package Main;

import Transmission.Message;
import Server.MachineInfo;
import Server.UDPServer;
import Transmission.EventMessage;
import Transmission.MessageReceivedListener;
import Transmission.failureDetect;
import Util.UtilityTool;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class DistributedMachine {

    private static Scanner in = new Scanner(System.in);
    private static UDPServer server;
    private static Logger logger = Logger.getLogger(DistributedMachine.class);
    private static MemberList memberList;
    private static CommandMap commandMap;
    private static UUID uuid;
    private static AtomicInteger timestamp;
    private static int port;
    private static MachineInfo.MachineState state;

    /**
     * Main entry of the program
     *
     * @param args
     */
    public static void main(String[] args) {
        log4jConfigure();
        try {
            init();
            work();
        } catch (Exception ex) {
            logger.fatal(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    private static void init() {

        commandMap = CommandMap.getInstance().initialize();
        memberList = new MemberList();
        uuid = UUID.randomUUID();
        timestamp = new AtomicInteger(0);
        state = MachineInfo.MachineState.Leaved;

        printWelcomeMessage();
        startUDPServer();

        server.getEventMessageQueue().addMessageReceivedListener(new MessageReceivedListener() {
            @Override
            public void onReceivingMessage(EventMessage m) throws ParserConfigurationException, TransformerException, UnknownHostException, IOException {
                logger.info("Receive EventMessage: " + m.getEventType());
            }
        });
    }

    private static void log4jConfigure() {
        PropertyConfigurator.configure("log4j.properties");
    }

    private static void work() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        while (true) {
            String cmd = inputCommand();
            String funcName;

            funcName = commandMap.findCommand(cmd);

            if (funcName == null) {
                wrongCommand();
            } else {
                Method method = DistributedMachine.class.getDeclaredMethod(funcName);
                method.setAccessible(true);
                method.invoke(null);    //static method
                method.setAccessible(false);
            }
        }
    }

    private static void wrongCommand() {
        System.out.println("Wrong command, please type 'help' for more information");
    }

    /**
     * Read input command from console.
     *
     * @return
     */
    private static String inputCommand() {
        System.out.print(">");
        return in.nextLine();
    }

    private static String inputAddress() {
        System.out.print("Input the address: ");
        String str = in.nextLine();
        if (UtilityTool.isIPAddress(str)) {
            return str;
        }
        return null;
    }

    private static int inputPortNumber() {
        System.out.print("Input the port: ");
        String str = in.nextLine();
        int ret;

        try {
            ret = Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            logger.info("Number format error");
            return 0;
        }
        return ret;
    }

    private static void printWelcomeMessage() {
        System.out.println("Welcome to the fictitious Group-R-Us Inc.!");
        System.out.println("Author: Muhammad Naveed, Junjie Hu");
        System.out.println("UUID: " + uuid.toString());
    }

    private static final Integer SYNC_PERIOD = 3000;

    /**
     * Start a UDP server that receives packets from other machines.
     */
    private static void startUDPServer() {
        while (true) {
            port = inputPortNumber();
            server = new UDPServer(port);
            try {
                server.start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while(true) {
                                Thread.sleep(SYNC_PERIOD);
                                if(memberList.size()!=0) {
                                    Sync();
                                }
                            }
                        } catch(Exception ex) {
                            ex.printStackTrace();
                            logger.error(ex.toString());
                        }
                    }
                }).start();
                return;
            } catch (BindException e) {
                System.out.println("Port is already in use");
                logger.info("UDP server port invalid");
            } catch (SocketException e) {
                e.printStackTrace();
                logger.error(e.toString());
            }
        }
    }


    public static void failureDetect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    failureDetect.detect();
                }
            }
        }).start();

    }

    private static void startContactServer() {
        int port = inputPortNumber();
        server = new UDPServer(port);
        try {
            server.start();
        } catch (SocketException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private static void connectContactServer() {
        String str = inputAddress();
        if (str == null) {
            System.out.println("Wrong ip address");
            return;
        }

        InetAddress address;
        String[] add = str.split(":");
        byte[] sendData;

        setStateConnected();

        try {
            DatagramSocket socket = new DatagramSocket();
            address = InetAddress.getByName(add[0]);

            Message joinMessage = Message.generateJoinMessage(address, uuid, timestamp.incrementAndGet());
            joinMessage.setServerPort(port);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            joinMessage.toxmlString(bos, getState());
            bos.close();
            sendData = bos.toByteArray();

            System.out.println(new String(sendData));

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(add[1]));
            socket.send(sendPacket);


            logger.info("join group, contact server: " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void leaveGroup() {
        try {
            DatagramSocket socket = new DatagramSocket();
            for (MachineInfo mi : getMemberList().getAll()) {
                String[] add = mi.getAddress().split(":");
                InetAddress address = InetAddress.getByName(add[0]);
                Message leaveMessage = Message.generateLeaveMessage(uuid, timestamp.incrementAndGet());

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                leaveMessage.toxmlString(bos, getState());
                bos.close();
                byte[] sendData;
                sendData = bos.toByteArray();

                System.out.println("leave address: " + address.toString() + ":" + add[1]);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(add[1]));
                socket.send(sendPacket);
            }
            getMemberList().clear();
            logger.info("Leave group");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }

    public static void Sync() throws ParserConfigurationException, TransformerException, UnknownHostException, IOException {

        MemberList list = DistributedMachine.getMemberList();


        byte[] sendData;
        DatagramSocket socket = new DatagramSocket();

        for (MachineInfo mi : list.getAll()) {
            InetAddress address = null;
            String str = mi.getAddress();
            String[] add = str.split(":");
            address = InetAddress.getByName(add[0]);

            Message syncMessage = Message.generateSyncMessage(address, uuid, timestamp.incrementAndGet());
            syncMessage.setServerPort(port);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            syncMessage.toxmlString(bos, mi.getState(), list);
            bos.close();
            sendData = bos.toByteArray();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(add[1]));
            socket.send(sendPacket);
        }

        logger.info("Sending sync message");
    }

    private static void showMemberList() {
        if (memberList.size() == 0) {
            System.out.println("no machine in the member list");
            return;
        }

        for (MachineInfo mi : memberList.getAll()) {
            System.out.println(mi.getAddress() + "\t" + mi.getState().toString() + "\t" + mi.getTimestamp() + "\t" + mi.getUUID());
        }
    }

    public static void printHelp() {
        CommandMap.printHelp();
    }

    public static void addMachine(MachineInfo mi) {
        if (!memberList.contains(mi)) {
            memberList.add(mi);
        } else {
            memberList.updateMachineInfo(mi);
        }
    }

    public static void removeMachine(MachineInfo mi) {
        if (memberList.contains(mi)) {
            memberList.remove(mi);
        }
    }

    public static void syncMachine(MachineInfo mi) {
        if(memberList.contains(mi)) {
            memberList.updateMachineInfo(mi);
        } else {
            memberList.add(mi);
        }
    }


    public static void syncMachine(EventMessage m) {

        for (MachineInfo mi : m.getMemberList().getAll()) {
            if (!memberList.contains(mi)) {
                memberList.add(mi);
            } else {
                memberList.updateMachineInfo(mi);
            }
        }

//        for (MachineInfo mi : memberList.getAll()) {
//            if (!m.getMemberList.getAll().contains(mi)) {
//                memberList.updateMachineInfo(mi);
//                
//        }




    }

    public static MemberList getMemberList() {
        return memberList;
    }

    public static AtomicInteger getTimestamp() {
        return timestamp;
    }

    public static UUID getUUID() {
        return uuid;
    }

    public static MachineInfo.MachineState getState() {
        return state;
    }

    public static void setStateConnected() {
        state = MachineInfo.MachineState.Connected;
    }
}

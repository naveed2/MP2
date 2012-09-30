import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class DistributedMachine {

    private static Scanner in = new Scanner(System.in);
    private static UDPServer server;
    private static Logger logger = Logger.getLogger(DistributedMachine.class);

    private static DistributedMachine instance;

    public static void main(String[] args) {
        log4jConfigure();
        try{
            work();
        } catch (Exception ex) {
            logger.fatal(ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    private static void log4jConfigure() {
        PropertyConfigurator.configure("log4j.properties");
    }

    private static void work() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        printWelcomeMessage();
        CommandMap commandMap = CommandMap.getInstance().initialize();

        while(true) {
            String cmd = inputCommand();
            String funcName;

            funcName = commandMap.findCommand(cmd);

            if(funcName == null) {
                wrongCommand();
            } else {
                Method method = DistributedMachine.class.getDeclaredMethod(funcName);
                method.setAccessible(true);
                method.invoke(null);    //static
                method.setAccessible(false);
            }
        }
    }

    private static void wrongCommand() {
        System.out.println("Wrong command, please type 'help' for more information");
    }

    private static String inputCommand() {
        System.out.print(">");
        return in.nextLine();
    }

    private static String inputAddress() {
        System.out.print("Input the address: ");
        String str = in.nextLine();
        if(UtilityTool.isIPAddress(str)) {
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
        } catch(NumberFormatException ex) {
            logger.info("Number format error");
            return 0;
        }
        return ret;
    }

    private static void printWelcomeMessage() {
        System.out.println("Welcome to the fictitious Group-R-Us Inc.!");
        System.out.println("Author: Muhammad Naveed, Junjie Hu");
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
        if(str == null) {
            System.out.println("Wrong ip address");
            return;
        }

        byte[] sendData = new byte[1024];
        sendData = "24242a".getBytes();
        String[] add = str.split(":");
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(add[0]);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, Integer.parseInt(add[1]));
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

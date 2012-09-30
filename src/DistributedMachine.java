import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void log4jConfigure() {
        PropertyConfigurator.configure("log4j.properties");
    }

    private static final String CMD_START_AS_CONTACT = "start contact server";

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

    private static int inputPortNumber() {
        System.out.print("Input the port: ");
        //TODO: not finished yet
        return 0;
    }

    private static void printWelcomeMessage() {
        System.out.println("Welcome to the fictitious Group-R-Us Inc.!");
        System.out.println("Author: Muhammad Naveed, Junjie Hu");
    }

    private static void startContactServer() {

//        server = new UDPServer()
    }
}

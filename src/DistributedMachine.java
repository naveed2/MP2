import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Scanner;

public class DistributedMachine {

    private static Scanner in = new Scanner(System.in);
    private static UDPServer server;
    private static Logger logger = Logger.getLogger(DistributedMachine.class);

    public static void main(String[] args) {
        log4jConfigure();
        work();
    }

    private static void log4jConfigure() {
        PropertyConfigurator.configure("log4j.properties");
    }

    private static void work() {
        printWelcomeMessage();

        //TODO:
//        server = new UDPServer();

        while(true) {
            String cmd = inputCommand();
        }
    }

    private static String inputCommand() {
        System.out.print(">");
        return in.nextLine();
    }

    private static void inputPortNumber() {

    }

    private static void printWelcomeMessage() {
        System.out.println("Welcome to the fictitious Group-R-Us Inc.!");
        System.out.println("Author: Muhammad Naveed, Junjie Hu");
    }
}

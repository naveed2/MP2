import java.util.Scanner;

public class DistributedMachine {

    private static Scanner in = new Scanner(System.in);
    private static UDPServer server;

    public static void main(String[] args) {
        work();
    }

    private static void work() {
        printWelcomeMessage();

        //TODO:
        server = new UDPServer();

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

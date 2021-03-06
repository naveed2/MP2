package Main;

import java.util.HashMap;
import java.util.Map;

/**
 * map command string -> function name
 * Singleton
 */
public class CommandMap {

    private static CommandMap instance = new CommandMap();
    private Map<String, String> stringToFuncName;

    private static final String CMD_START_CONTACT_SERVER = "start contact server";
    private static final String FUNC_START_CONTACT_SERVER = "startContactServer";

    private static final String CMD_CONNECT_CONTACT_SERVER = "connect";
    private static final String FUNC_CONNECT_CONTACT_SERVER = "connectContactServer";

    private static final String CMD_LEAVE_GROUP = "leave";
    private static final String FUNC_LEAVE_GROUP = "leaveGroup";

    private static final String CMD_SHOW_MEMBER_LIST = "show";
    private static final String FUNC_SHOW_MEMBER_LIST = "showMemberList";

    private static final String CMD_SET_DROP_RATES = "set drop rates";
    private static final String FUNC_SET_DROP_RATES = "setDropRates";

    private static final String CMD_HELP = "help";
    private static final String FUNC_HELP = "printHelp";

    private static final String CMD_QUIT = "quit";
    private static final String FUNC_QUIT ="quit";

    private CommandMap() {

    }

    public static CommandMap getInstance() {
        return instance;
    }

    public synchronized CommandMap initialize() {
        instance = new CommandMap();
        stringToFuncName = new HashMap<String, String>();

        stringToFuncName.put(CMD_START_CONTACT_SERVER, FUNC_START_CONTACT_SERVER);
        stringToFuncName.put(CMD_CONNECT_CONTACT_SERVER, FUNC_CONNECT_CONTACT_SERVER);
        stringToFuncName.put(CMD_LEAVE_GROUP, FUNC_LEAVE_GROUP);
        stringToFuncName.put(CMD_SHOW_MEMBER_LIST, FUNC_SHOW_MEMBER_LIST);
        stringToFuncName.put(CMD_SET_DROP_RATES, FUNC_SET_DROP_RATES);
        stringToFuncName.put(CMD_HELP, FUNC_HELP);
        stringToFuncName.put(CMD_QUIT, FUNC_QUIT);

        return this;
    }

    private static final String FORMAT_STRING = "%-25s%-25s\n";

    public static void printHelp() {
        System.out.printf(FORMAT_STRING, "COMMAND", "USAGE");
        System.out.printf(FORMAT_STRING, CMD_START_CONTACT_SERVER, "start as a contact server");
        System.out.printf(FORMAT_STRING, CMD_CONNECT_CONTACT_SERVER, "connect the contact server");
        System.out.printf(FORMAT_STRING, CMD_LEAVE_GROUP, "leave group");
        System.out.printf(FORMAT_STRING, CMD_SHOW_MEMBER_LIST, "show the member list");
        System.out.printf(FORMAT_STRING, CMD_SET_DROP_RATES, "set drop rates");
        System.out.printf(FORMAT_STRING, CMD_QUIT, "quit");
    }

    public synchronized String findCommand(String cmd) {
        return stringToFuncName.get(cmd);
    }

}

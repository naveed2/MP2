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

    private static final String CMD_SHOW_MEMBER_LIST = "show";
    private static final String FUNC_SHOW_MEMBER_LIST = "showMemberList";

    private static final String CMD_HELP = "help";
    private static final String FUNC_HELP = "printHelp";

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
        stringToFuncName.put(CMD_SHOW_MEMBER_LIST, FUNC_SHOW_MEMBER_LIST);
        stringToFuncName.put(CMD_HELP, FUNC_HELP);

        return this;
    }

    private static final String FORMAT_STRING = "%-25s%-25s\n";

    public static void printHelp() {
        System.out.printf(FORMAT_STRING, "COMMAND", "USAGE");
        System.out.printf(FORMAT_STRING, CMD_START_CONTACT_SERVER, "start as a contact server");
        System.out.printf(FORMAT_STRING, CMD_CONNECT_CONTACT_SERVER + " ip:port", "connect the contact server");
        System.out.printf(FORMAT_STRING, CMD_SHOW_MEMBER_LIST, "show the member list");
    }

    public synchronized String findCommand(String cmd) {
        return stringToFuncName.get(cmd);
    }

}

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

        return this;
    }

    public synchronized String findCommand(String cmd) {
        return stringToFuncName.get(cmd);
    }

}

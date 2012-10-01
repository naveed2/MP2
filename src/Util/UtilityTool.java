package Util;

import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UtilityTool {

    private static Logger logger = Logger.getLogger(UtilityTool.class);

    public static boolean isIPAddress(String str) {
        String[] res = str.split(":");
        if(res.length!=2) {
            return false;
        }

        String ip;
        Integer port;
        try{
            port = Integer.parseInt(res[1]);
        } catch(NumberFormatException ex) {
            return false;
        }

        if(port<=0 || port >=65536) {
            return false;
        }

        ip = res[0];

        String regex;

        regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$" ;
        return ip.matches(regex);
    }

    public static InetAddress getInetAddress(String ip) {

        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

}

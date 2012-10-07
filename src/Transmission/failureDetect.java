package Transmission;

import Main.DistributedMachine;
import Main.MemberList;
import Server.MachineInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: naveed2
 * Date: 10/6/12
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class failureDetect {

    public static void main(String[] args){
        System.out.println(ping("localhost"));
        System.out.println(ping("192.17.11.65"));
    }

    public static void detect(){

        MemberList list = DistributedMachine.getMemberList();
        for (MachineInfo mi : list.getAll()){
            if(!ping(mi.getIP()))
                list.remove(mi);
        }

    }


    public static boolean ping(String IP) {
        // 4 seconds because it will take sometime to go through the code, will
        // think about this later
            Message msg = new Message();
            msg._messageType = 3;



        return false;
    }


}

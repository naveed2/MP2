package Transmission;

import Main.DistributedMachine;
import Main.MemberList;
import Server.MachineInfo;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.Date;

/**
 * Used in failure detecting.
 */
public class FailureDetector {

    final private MachineInfo mi;
    private Thread thread;
    private boolean stop;
    private static Logger logger = Logger.getLogger(FailureDetector.class);

    private static final Integer WAIT_TIME = 4000; //ms

    public FailureDetector(final MachineInfo mi) {
        this.mi = mi;
        stop = false;

        thread = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(WAIT_TIME);
                        break;
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }

                if(!stop) {
                    Date date = new Date();
                    FailureDetector.this.mi.setStateFailed();
                    String str = date.toString() +  ": Detect fail machine " + mi.getAddress() + " " + mi.getUUID();
                    logger.error(str);
                    System.out.println(str);
                }
            }
        });
    }

    public void startDetect(){
        synchronized (this) {
            stop = false;
            thread.start();
        }
    }

    public void stopDetect() {
        synchronized (this) {
            stop = true;
            thread.interrupt();
        }
    }

    public void receiveAck() {
        thread.interrupt();
    }


    public static boolean ping(String IP) {
        // 4 seconds because it will take sometime to go through the code, will
        // think about this later
            Message msg = new Message();
            msg._messageType = 3;



        return false;
    }


}

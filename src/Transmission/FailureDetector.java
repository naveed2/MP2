package Transmission;

import Main.DistributedMachine;
import Main.MemberList;
import Server.MachineInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Used in failure detecting.
 */
public class FailureDetector {

    final private MachineInfo mi;
    private Thread thread;
    private boolean stop;

    private static final Integer WAIT_TIME = 5000; //ms

    public FailureDetector(MachineInfo mi) {
        this.mi = mi;
        stop = false;

        thread = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(WAIT_TIME);
                        break;
                    } catch (InterruptedException e) {
                        if(stop) {
                            return;
                        }
                    }
                }

                FailureDetector.this.mi.setStateFailed();
            }
        });
    }

    public void startDetect(){
        stop = false;
        thread.start();
    }

    public void stopDetect() {
        stop = true;
        thread.interrupt();
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

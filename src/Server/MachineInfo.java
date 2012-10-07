package Server;

import Transmission.FailureDetector;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MachineInfo {
    private String ip;
    private Integer port;
    private UUID uuid;
    private AtomicInteger timestamp;
    private MachineState state;
    private FailureDetector failureDetector;

    public enum MachineState {
        Connected, Failed, Leaved
    }

    public MachineInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
        timestamp = new AtomicInteger(0);

        failureDetector = new FailureDetector(this);
    }

    public MachineInfo setUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public MachineInfo setTimestamp(int timestamp) {
        synchronized (this) {
            this.timestamp.set(timestamp);
        }
        return this;
    }

    public Integer getTimestamp() {
        synchronized (this) {
            return timestamp.get();
        }
    }

    public MachineInfo updateState(MachineInfo mi) {
        this.state = mi.getState();
        return this;
    }

    public void maximizeTimestamp(int timestamp) {
        synchronized (this) {
            if(this.timestamp.get() < timestamp) {
                this.timestamp.set(timestamp);
            }
        }
    }

    public String getAddress() {
        return ip + ":" + port;
    }
    
    public String getIP() {
        return ip;
    }
    
    public Integer getPort() {
        return port;
    }

    public UUID getUUID() {
        return uuid;
    }

    public MachineInfo setStateConnected() {
        synchronized (this) {
            state = MachineState.Connected;
        }
        return this;
    }

    public MachineInfo setStateFailed() {
        synchronized (this) {
            state = MachineState.Failed;
        }
        return this;
    }

    public MachineInfo setStateLeaved() {
        synchronized (this) {
            state = MachineState.Leaved;
        }
        return this;
    }

    public MachineInfo setState(MachineState state) {
        synchronized (this) {
            this.state = state;
        }
        return this;
    }

    public void startFailureDetecting() {
        failureDetector.startDetect();
    }

    public void stopFailureDetecting() {
        failureDetector.stopDetect();
    }

    public void receivePingAck() {
        failureDetector.receiveAck();
    }


    public MachineState getState() {
        return state;
    }

    public static void main(String args[]) {
        System.out.println(MachineInfo.MachineState.valueOf("Connected").toString());
    }
}

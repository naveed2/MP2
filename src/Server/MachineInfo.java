package Server;

import java.util.UUID;

public class MachineInfo {
    private String ip;
    private Integer port;
    private UUID uuid;
    private int timestamp;
    private MachineState state;

    public enum MachineState {
        Connected, Failed,
    }

    public MachineInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public MachineInfo setUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public MachineInfo setTimestamp(int timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getAddress() {
        return ip + ":" + port;
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
}

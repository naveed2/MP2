package Transmission;

import Server.MachineInfo;

/**
 * When <code>{@link Server.UDPServer}</code> receives a message, it will pack a {@link EventMessage}
 */
public class EventMessage {

    public enum EventType {
        Join, Leave;
    }

    private MachineInfo machineInfo;
    private EventType type;

    public EventMessage(EventType type) {
        this.type = type;
    }

    public EventType getEventType() {
        return type;
    }

    public EventMessage setMachineInfo(MachineInfo mi) {
        this.machineInfo = mi;
        return this;
    }

    public MachineInfo getMachineInfo() {
        return this.machineInfo;
    }
}

package Transmission;

import Server.MachineInfo;
import Main.MemberList;

/**
 * When <code>{@link Server.UDPServer}</code> receives a message, it will pack a {@link EventMessage}
 */
public class EventMessage {

    public enum EventType {
        Join, Leave, Sync, Ping, Ping_ACK;
    }

    private MachineInfo machineInfo;
    private EventType type;
    private MemberList list;
    

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
    
    public MemberList getMemberList(){
        return list;
    }
    
    public EventMessage setMemberList(MemberList list){
        this.list = list;
        return this;
    }
    
}

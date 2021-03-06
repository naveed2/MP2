package Main;

import Server.MachineInfo;

import java.util.*;

public class MemberList {

    private List<MachineInfo> list;

    public MemberList() {

        list = new LinkedList<MachineInfo>();
    }

    public void add(MachineInfo mi) {
        if (mi.getUUID().equals(DistributedMachine.getUUID())) { //don't need to add itself in memberlist
            return;
        }
        synchronized (this) {
            if (!this.contains(mi)) {
                list.add(mi);
            }
        }
    }

    public void remove(MachineInfo mi) {
        synchronized (this) {
            list.remove(mi);
        }
    }

    public MachineInfo get(int i) {
        synchronized (this) {
            return list.get(i);
        }
    }

    public MachineInfo get(MachineInfo mi) {
        synchronized (this) {
            for(MachineInfo tmp : getAll()) {
                if (tmp.getUUID().equals(mi.getUUID())) {
                    return tmp;
                }
            }
        }
        return null;
    }

    public List<MachineInfo> getAll() {
        synchronized (this) {
            return new LinkedList<MachineInfo>(list);
        }
    }

    public int size() {
        synchronized (this) {
            return list.size();
        }
    }

    public boolean contains(MachineInfo mc) {
        synchronized (this) {
            for (MachineInfo tmp : list) {
                if (tmp.getUUID().equals(mc.getUUID())) {
                    return true;
                }
            }
            return false;
        }
    }

    public void updateMachineInfo(MachineInfo mi) {
        synchronized (this) {
            for (MachineInfo cur : list) {
                if (cur.getUUID().equals(mi.getUUID())) {
                    if (cur.getTimestamp() < mi.getTimestamp()) {
                        cur.setTimestamp(mi.getTimestamp());
                        cur.updateState(mi);
                    }
                    return;
                }
            }
        }
    }

    public void clear() {
        synchronized (this) {
            list = new LinkedList<MachineInfo>();
        }
    }
}

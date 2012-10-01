import java.util.*;


public class MemberList {
	

    private List<MachineInfo> list;
	
	public MemberList(){

		list = new LinkedList<MachineInfo>();
	}


    public void add(MachineInfo mc) {
        synchronized (this) {
            list.add(mc);
        }
    }
	

	public void remove(String key){
        synchronized (this) {
		    list.remove(key);
        }
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
            for(MachineInfo tmp : list) {
                if(tmp.getUUID().equals(mc.getUUID())) {
                    return true;
                }
            }
            return false;
        }
    }

}

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MemberList {
	
	private Map<String, MachineInfo> list = null;
	
	public MemberList(){
		list = new HashMap<String, MachineInfo>();
		
	}
	
	public void add(String IP, MachineInfo ID){
		list.put(IP, ID);
	}
	
	public Set<String> keySet(){
		return list.keySet(); 
	}
	
	public void remove(String key){
		list.remove(key);
	}

}

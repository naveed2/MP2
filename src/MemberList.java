import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MemberList {
	
	Map<String, MachineInfo2> list = null;
	
	public MemberList(){
		list = new HashMap<String, MachineInfo2>();
		
	}
	
	public void add(String IP, MachineInfo2 ID){
		list.put(IP, ID);
	}
	
	public Set<String> keySet(){
		return list.keySet(); 
	}
	
	public void remove(String key){
		list.remove(key);
	}

}

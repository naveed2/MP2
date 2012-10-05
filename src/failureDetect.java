import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class failureDetect {

//	public MemberList detect(Main.MemberList list) throws UnknownHostException, IOException {
//
//		for (String key : list.keySet()) {
//			if (!ping(key)) {
//				list.remove(key);
//				System.out.println("Failed to connect host " + key.toString());
//			}
//		}
//		return list;
//
//	}

	public boolean ping(String IP) throws UnknownHostException, IOException {
		// 4 seconds because it will take sometime to go through the code, will
		// think about this later
		return InetAddress.getByAddress(IP.getBytes()).isReachable(4000); //put characterset for platform indepdenence
	}

}

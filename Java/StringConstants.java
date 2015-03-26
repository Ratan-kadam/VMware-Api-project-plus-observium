
import java.util.HashMap;

public class StringConstants {
	public static String VCENTER_ADMIN_URL = "https://192.168.43.128/sdk";
	public static String VCENTER_URL = "https://192.168.43.128/sdk";
	
	public static String VCENTER_PASSWORD = "missionteam3";
	public static String VCENTER_USERNAME = "root";
	public static int SNAPSHOT_INTERVAL = 10 * 60; // seconds
	public static int PERFORMANCE_STAT_INTERVAL = 30; // seconds
	public static int VM_PING_INTERVAL = 10; // seconds
	public static int PING_NUMBER = 4; // seconds
	
	public final static HashMap<String, String> vHosts = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("130.65.132.242", "T12-vHost02-cum3_IP=.132.242");
			put("130.65.132.243", "T12-vHost03-cum3_IP=.132.243");
			put("130.65.132.241", "T12-vHost01-cum3_IP=.132.241");
		}
	};

}

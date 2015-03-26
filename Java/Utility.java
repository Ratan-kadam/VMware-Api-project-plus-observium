
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;


public class Utility {
	//Returns the vHost for a specific Virtual Machine.
	public static HostSystem getHostSystemForVM(VirtualMachine vmEntity){
		HostSystem host = null;
		try {
			ManagedEntity[] hostsEntities = new InventoryNavigator(AvailabilityManager.si.getRootFolder()).searchManagedEntities("HostSystem");
			for(int i=0; i<hostsEntities.length; i++){
				//System.out.println("host["+i+"]=" + hosts[i].getName());
				HostSystem h = (HostSystem) hostsEntities[i];
				VirtualMachine vmsEntities[] = h.getVms();
				for (int p = 0; p < vmsEntities.length; p++) {
					VirtualMachine v = (VirtualMachine) vmsEntities[p];
					if (v.getName().toLowerCase().equals(vmEntity.getName().toLowerCase())) {
						host = h;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error " + e.getMessage());
		}
		return host;
	}
	
	//Returns a VM object from the InventoryNavigator using vm name
	public static VirtualMachine getVirtualMachineFromName(String vmEntityName){
		VirtualMachine vm = null;
		try {
			if(vmEntityName != null || vmEntityName.length() > 0)
				vm = (VirtualMachine) new InventoryNavigator(AvailabilityManager.si.getRootFolder()).searchManagedEntity("VirtualMachine", vmEntityName);
		} catch (InvalidProperty e) {
			System.out.println("Error in : " + e.getMessage());
		} catch (RuntimeFault e) {
			System.out.println("Error in : " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Error in : " + e.getMessage());
		}
		return vm;
	}
	
	//Return all Hosts entities from the InventoryNavigator
	public static ManagedEntity[] getAllHosts(){
		ManagedEntity[] mes = null;
		try {
			mes = new InventoryNavigator(AvailabilityManager.si.getRootFolder()).searchManagedEntities("HostSystem");
		} catch (Exception e) {
			System.out.println("Error in getAllHosts: " + e.getMessage());
		}
		return mes;
	}
	
	//Return all VM entities from the InventoryNavigator
	public static ManagedEntity[] getAllVirtualMachine(){
		ManagedEntity[] mes = null;
		try {
			mes = new InventoryNavigator(AvailabilityManager.si.getRootFolder()).searchManagedEntities("VirtualMachine");
		} catch (Exception e) {
			System.out.println("Error in getAllVirtualmachine: " + e.getMessage());
		}
		return mes;
	}
	

	
	
	
	public static boolean pingIP(String ip) throws Exception {
		String cmd = "";
		
		if (System.getProperty("os.name").startsWith("Windows")) {
			// For Windows
			cmd = "ping -n 3 " + ip;
		} else {
			// For Linux and OSX
			cmd = "ping -c 3 " + ip;
		}
		
		System.out.println("\nPing Operation:  "+ ip + "......");
		Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();		
		return process.exitValue() == 0;
	}
	
	
	
	public static boolean ping(String ip) throws Exception {
		int time = 0;

		while (!pingIP(ip)) {						
			time++;
			if (time >= StringConstants.PING_NUMBER)
				return false;
		}
		return true;
	}

	//Return all VM entities from the InventoryNavigator
	public static ManagedEntity[] getAllVirtualMachineFromvCenter(){
		ManagedEntity[] mes = null;
		try {
			mes = new InventoryNavigator(AvailabilityManager.siOfAdmin.getRootFolder()).searchManagedEntities("VirtualMachine");
		} catch (Exception e) {
			System.out.println("Error in getAllVirtualmachine: " + e.getMessage());
		}
		return mes;
	}

	public static ServiceInstance getSiForVCenterAdmin(){
		URL url;
		ServiceInstance si = null; 
		try {
			url = new URL(StringConstants.VCENTER_ADMIN_URL);
			si = new ServiceInstance(url, StringConstants.VCENTER_USERNAME, StringConstants.VCENTER_PASSWORD, true);
		} catch (Exception e) {
			System.out.println("Error in getSiForVCenter");
		}
		return si;
	}

	
	public static ServiceInstance getSiForVCenter(){
		URL url;
		ServiceInstance si = null; 
		try {
			url = new URL(StringConstants.VCENTER_URL);
			si = new ServiceInstance(url, StringConstants.VCENTER_USERNAME, StringConstants.VCENTER_PASSWORD, true);
		} catch (Exception e) {
			System.out.println("Error in getSiForVCenter");
		}
		return si;
	}
	
	

}

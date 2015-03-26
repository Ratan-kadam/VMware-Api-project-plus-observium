
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


public class OtherUtils {
	//Returns the vHost for a specific Virtual Machine.
	public static HostSystem getHostSystemForVM(VirtualMachine vm){
		HostSystem host = null;
		try {
			ManagedEntity[] hosts = new InventoryNavigator(AvailabilityManager.si.getRootFolder()).searchManagedEntities("HostSystem");
			for(int i=0; i<hosts.length; i++){
				//System.out.println("host["+i+"]=" + hosts[i].getName());
				HostSystem h = (HostSystem) hosts[i];
				VirtualMachine vms[] = h.getVms();
				for (int p = 0; p < vms.length; p++) {
					VirtualMachine v = (VirtualMachine) vms[p];
					if (v.getName().toLowerCase().equals(vm.getName().toLowerCase())) {
						host = h;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error in getHostSystem" + e.getMessage());
		}
		return host;
	}
	
	//Return any random active vHost 
	//if it does not exist, then you need to create a new VHost
	public static HostSystem getAlternateActiveHost(HostSystem oldHost){
		HostSystem host = null;
		try {
			ManagedEntity[] hosts = new InventoryNavigator(AvailabilityManager.si.getRootFolder()).searchManagedEntities("HostSystem");
			
			if(hosts.length > 1){
				for(int i=0; i<hosts.length; i++){
					//System.out.println("host["+i+"]=" + hosts[i].getName());
					HostSystem h = (HostSystem) hosts[i];
					if(h.getName().toLowerCase().equals(oldHost.getName().toLowerCase())){
						//This is the old vHost which is inActive. 
						//So, ignore this vHost and search for a new vHost.
						continue;
					}else{
						MonitorHostSystem m = new MonitorHostSystem(h);
						m.setNoOfIterations(1);
						//m.start();
						//m.join();
						if(m.isRunning()){
							host = h;
							System.out.println("HOST _" + host.getName() + " - Is Active and Selected");
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error in getHostSystem");
		}
		return host;
	}
	
	//Returns a VM object from the InventoryNavigator using vm name
	public static VirtualMachine getVirtualMachineFromName(String vmName){
		VirtualMachine vm = null;
		try {
			if(vmName != null || vmName.length() > 0)
				vm = (VirtualMachine) new InventoryNavigator(AvailabilityManager.si.getRootFolder()).searchManagedEntity("VirtualMachine", vmName);
		} catch (InvalidProperty e) {
			System.out.println("Error in getVirtualMachingFromName: " + e.getMessage());
		} catch (RuntimeFault e) {
			System.out.println("Error in getVirtualMachingFromName: " + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("Error in getVirtualMachingFromName: " + e.getMessage());
		}
		return vm;
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
	public static ManagedEntity[] getAllVirtualMachineFromvCenter(){
		ManagedEntity[] mes = null;
		try {
			//mes = new InventoryNavigator(AvailabilityManager.siAdmin.getRootFolder()).searchManagedEntities("VirtualMachine");
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
		
		System.out.println("Ping "+ ip + "......");
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

	
	public static boolean checkIfUnderMaintenance(String entityName){
		boolean exists = false;
		System.out.println("UM: Check if under maintenance: " + entityName);
		try {
			if(entityName != null){
				/*if(AvailabilityManager.m_underMaintenance != null){
					if(AvailabilityManager.m_underMaintenance.size() > 0){
						if(AvailabilityManager.m_underMaintenance.contains(entityName)){
							exists = true;
						}						
					}
				}*/
			}
		} catch (Exception e) {
			System.out.println("Error in checkIfUnderMaintenance: "+e.getMessage());
		}
		if(exists)
			System.out.println("UM: Under maintenace\n");
		else
			System.out.println("UM: Not Under maintenace\n");
			
		return exists;
	}
	
	public static void removeVMFromUnderMaintenance(String entityName){
		try {
			if(entityName != null){
				/*if(AvailabilityManager.m_underMaintenance != null){
					if(AvailabilityManager.m_underMaintenance.size() > 0){
						if(AvailabilityManager.m_underMaintenance.contains(entityName)){
							System.out.println("Removing from under maintenance : " + entityName + "\n");
							AvailabilityManager.m_underMaintenance.remove(entityName);
						}						}
				}*/
			}
		} catch (Exception e) {
			System.out.println("Error in removeVMFromUnderMaintenance: "+e.getMessage());
		}
	}
	
	public static void addVMToUnderMaintenance(String entityName){
		if(entityName != null){
			/*if(!AvailabilityManager.m_underMaintenance.contains(entityName)){
				System.out.println("Adding to under maintenance" +  entityName + "\n");
				AvailabilityManager.m_underMaintenance.add(entityName);
			}*/
		}
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

}

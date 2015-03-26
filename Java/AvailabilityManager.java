
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.print.attribute.standard.MediaSize.Other;

import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;


public class AvailabilityManager {

	public static ServiceInstance si;
	public static ServiceInstance siOfAdmin = null;
	public static Alarm powerOffAlarm;
	
	public static void main(String[] args) throws Exception{
		si = Utility.getSiForVCenter();
		if(si != null){
			ManagedEntity[] mes = null;
			try {
				//get all the VM entities
				mes = Utility.getAllVirtualMachine();
				if(mes != null || mes.length > 0){
					//set alarm on the dataCenter
					for(int i = 0; i < mes.length; i++){
						VirtualMachine vm = (VirtualMachine)mes[i];
						if(vm != null){
							if(vm.getGuest().getIpAddress() != null){
								//Create thread to monitor each VM
								MonitorVirtualMachine monitorVM = new MonitorVirtualMachine(vm);
								
								if(monitorVM.isRunning()){
									System.out.println("RUNNING - " + monitorVM.getHostIP());		
									
								}else{
									// Revert the VM
									
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

}

import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.samples.StringConstants;


public class DisasterRecovery {

	private static Vector m_managedVMEntities = new Vector();
	public static ServiceInstance si;

	public static void main(String[] args) throws Exception{
		URL url = new URL(StringConstants.VCENTER_URL);
		si = new ServiceInstance(url, StringConstants.VCENTER_USERNAME, StringConstants.VCENTER_PASSWORD, true);
		ManagedEntity[] mes = null;
		try {
			//get all the VM entities
			mes = OtherUtils.getAllVirtualMachine();
			if(mes != null || mes.length > 0){
				//Thread to monitor the inventory
				MonitorInventory monitorInventory = new MonitorInventory();
				monitorInventory.start();
				
				//Create thread to take snapshots of each VM after every 10min.
				SnapshotManager monitorSnapShot = new SnapshotManager();
				monitorSnapShot.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}

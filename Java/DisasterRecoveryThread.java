import java.rmi.RemoteException;
import java.util.*;

import com.vmware.vim25.FileFault;
import com.vmware.vim25.InsufficientResourcesFault;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VmConfigFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;
import com.vmware.vim25.mo.samples.vm.VMSnapshot;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.mo.Task;

public class DisasterRecoveryThread {
	
	@SuppressWarnings("null")
	public void startRecoveringSystems(ServiceInstance sin)
	{
		ServiceInstance si = sin;
	    AvailabilityManager.si = sin;
	    int count = 0;
	    
	    if (si != null) {
			ManagedEntity[] mes = null;
			ManagedEntity[] collection = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {
					VirtualMachine vm = (VirtualMachine) mes[i];
					 System.out.println("Prev ShutDown:" + vm.getName() + " -> "+ vm.getSummary().runtime.cleanPowerOff);
					if(vm.getSummary().runtime.cleanPowerOff != null){
					 if(!vm.getSummary().runtime.cleanPowerOff)
					 {
						 System.out.println("Machine:" + vm.getName() + " shutdown abnormally " );
						
						 int len = vm.getSnapshot().getRootSnapshotList().length;
						System.out.println(" vm.getSnapshot().getRootSnapshotList()[len-1].getName():" + vm.getSnapshot().getRootSnapshotList()[len-1].getName());
						    revertsnap rs = new revertsnap();
							rs.revertPrevSnapshot(si, vm.getName(), vm.getSnapshot().getRootSnapshotList()[len-1].getName());
						
							
						 System.out.println("Restoring to current Snapshot..");
						System.out.println("Starting all the Machines ..");
						/*startAllVirtualMachines SVM = new startAllVirtualMachines();
						SVM.startAllVMs(si);*/
						 System.out.println("______________________________________________");
						 
						 
					 }
					}
					
					
					 
				}
				
			}
	    }
	    
	}

}

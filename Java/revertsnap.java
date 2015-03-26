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

public class revertsnap {
	
public void revertsnapshot(ServiceInstance sin)
{
	ServiceInstance si = sin;
	AvailabilityManager.si = sin; 
	Scanner inpVM = new Scanner(System.in);
	System.out.println("Enter the Machine name ");
	String VMname = inpVM.nextLine();
	System.out.println("Enter the SnapShotName");
	String SNAPname= inpVM.nextLine();
	
	ManagedEntity[] mes = null;
	mes = Utility.getAllVirtualMachine();
	if (mes.length > 0) {
		for (int i = 0; i < mes.length; i++) {
			VirtualMachine vm = (VirtualMachine) mes[i];
			if (vm.getName().equals(VMname)) {
				VirtualMachineSnapshot vmsnap = VMSnapshot.getSnapshotInTree(vm, SNAPname);
				try {
					System.out.println("Snap reverting started..");
					Task task =vmsnap.revertToSnapshot_Task(null);
					
				} catch (Exception e) {
					System.out.println(" Snapshot reverting problem..");
					
				}
				
			}
		}
	}
	
}

///////
public void revertPrevSnapshot(ServiceInstance sin, String Vname, String SNAPname)
{
	ServiceInstance si = sin;
	AvailabilityManager.si = sin; 
	/*Scanner inpVM = new Scanner(System.in);
	System.out.println("Enter the Machine name ");
	String VMname = inpVM.nextLine();
	System.out.println("Enter the SnapShotName");
	String SNAPname= inpVM.nextLine();*/
	
	ManagedEntity[] mes = null;
	mes = Utility.getAllVirtualMachine();
	if (mes.length > 0) {
		for (int i = 0; i < mes.length; i++) {
			VirtualMachine vm = (VirtualMachine) mes[i];
			if (vm.getName().equals(Vname)) {
				VirtualMachineSnapshot vmsnap = VMSnapshot.getSnapshotInTree(vm, SNAPname);
				try {
					System.out.println("Snap reverting started..");
					Task task =vmsnap.revertToSnapshot_Task(null);
					
				} catch (Exception e) {
					System.out.println(" Snapshot reverting problem..");
					
				}
				
			}
		}
	}
	
}




}

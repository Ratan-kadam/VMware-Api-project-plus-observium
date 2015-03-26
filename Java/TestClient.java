import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class TestClient {

	public static void main(String[] args) {
		ServiceInstance si = Utility.getSiForVCenter();
		AvailabilityManager.si = si;
		

		if (si != null) {
			ManagedEntity[] mes = null;
			try {
				// get all the VM entities
				mes = Utility.getAllVirtualMachine();
				if(mes.length > 0) {
					System.out.println("******Number of total VMS : " + mes.length);
					for(int i = 0; i < mes.length; i++){
						VirtualMachine vm = (VirtualMachine)mes[i];
						if(vm != null){
							if(vm.getGuest().getIpAddress() != null){
								System.out.println("*******IP: " + vm.getGuest().getIpAddress());
								//Create thread to monitor each VM
								MonitorVirtualMachine monitorVM = new MonitorVirtualMachine(vm);
								
								if(monitorVM.isRunning()){
									//System.out.println("RUNNING - " + monitorVM.getHostIP());
									//System.out.println("Name : " + vm.getName());
									
									VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
								//	System.out.println(summary.toString());
									VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
									
								//	System.out.println("Power State - " + vmri.getPowerState());
									if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn
										&& "Ratan1".equals(vm.getName()))
									{
										//HostSystem host = (HostSystem) new InventoryNavigator(vm.getParent()).searchManagedEntity("HostSystem", "130.65.132.242");
										
										//Task task = vm.powerOffVM_Task();
										//task.waitForMe();
									//	System.out.println("vm:" + vm.getName() + " ----powered off.--" + ((VirtualMachineRuntimeInfo)vm.getRuntime()).getPowerState());
									}
								}else{
									// Revert the VM
									
								}
							}
						}
					}
				}
			} catch (Exception e) {

			}
		}
	}
}

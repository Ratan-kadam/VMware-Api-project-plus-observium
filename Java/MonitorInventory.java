import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;


public class MonitorInventory extends Thread {

	@Override
	public void run() {
		try {
			//get a list of all the machines and then take snapshot of individual machine
			ManagedEntity[] mes = OtherUtils.getAllVirtualMachine();

			for(int i=0; i < mes.length; i++){
				VirtualMachine vm = (VirtualMachine)mes[i];
				if(vm != null){
					if(vm.getGuest().getIpAddress() != null){
						System.out.println("IP ADDRESS: " + vm.getGuest().getIpAddress() +" --- NAME: " + vm.getName());
						//Configure Alarm for each VM Entity
						new AlarmConfiguration(vm);

						//Create thread to monitor each VM
						MonitorVirtualMachine monitorVM = new MonitorVirtualMachine(vm);
						//monitorVM.start();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error in SnapShotManager: "+ e.getMessage());
		}
	}

}

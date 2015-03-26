import com.vmware.vim25.VirtualMachineSnapshotInfo;
import com.vmware.vim25.VirtualMachineSnapshotTree;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;
import com.vmware.vim25.mo.samples.StringConstants;


public class SnapshotManager extends Thread {

	private VirtualMachine m_vm;
	public SnapshotManager(){
	}
	
	@Override
	public void run() {
		try {
			//get a list of all the machines and then take snapshot of individual machine
			ManagedEntity[] mes = OtherUtils.getAllVirtualMachine();
			for(int i=0; i < mes.length; i++){
				VirtualMachine vm = (VirtualMachine)mes[i];
				if(vm != null){
					//Create a snap shot for a VM
					setVM(vm);
					createNewSnapshot(vm);
				}
			}
		} catch (Exception e) {
			System.out.println("Error in SnapShotManager: "+ e.getMessage());
		}
	}
	
	private void createNewSnapshot(VirtualMachine vm){
		//Create a snap shot for a VM
		Thread snapThread = new Thread() {
		      public void run(){
					Task taskObj = null;
					String description = "backUp_" + vm.getName() + "_" + System.currentTimeMillis();
					try {
						//Before creating new snapshot for each machine, check if there are previous snapshots available.
						//Delete all the previous snapshots and then create a new snapshot.
						removePreviousSnapshots();

						//Start creating snapshot for each VM separately.
						System.out.println("Creating Snapshot for VM: " + vm.getName() + "---" + vm.getGuest().getIpAddress());
						taskObj = vm.createSnapshot_Task(vm.getName(), description, true, true);
						if(taskObj.waitForMe() == Task.SUCCESS){
							System.out.println("Snapshot of VM - " + vm.getName() + "- Created");
						}
					} catch (Exception e) {
						System.out.println("Error in SnapshotManager - sub-thread: " + e.getMessage());
					} 
		      }
		   };
		   snapThread.start();

		   try{
    		   snapThread.sleep(StringConstants.SNAPSHOT_INTERVAL);
           } catch(Exception e){
        	   System.out.println("Error in creating new Snapshot: " + e.getMessage());
           }
		
	}
	
	private void removePreviousSnapshots(){
		//Removes all the previous snapshots in the tree created for a VM 
	    VirtualMachineSnapshotInfo snapInfo = getVM().getSnapshot();
	    if(snapInfo != null){
	    VirtualMachineSnapshotTree[] snapTree = snapInfo.getRootSnapshotList();
		    if(snapTree.length > 1){
		    	//delete all the previous snapshots
		    	try {
					getVM().removeAllSnapshots_Task();
				} catch (Exception e) {
					System.out.println("Error in removePreviousSnapshots: "+ e.getMessage());
				}
		    }
	    }
	}
	
	public static VirtualMachineSnapshot getCurrentSnapShot(VirtualMachine vm){
		VirtualMachineSnapshot vmSnapShot = null;
		try {
			vmSnapShot = null;
			if(vm != null){
				vmSnapShot = vm.getCurrentSnapShot();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return vmSnapShot;
	}

	public VirtualMachine getVM() {
		return m_vm;
	}

	public void setVM(VirtualMachine m_vm) {
		this.m_vm = m_vm;
	}

}

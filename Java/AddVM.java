import java.rmi.RemoteException;

import com.vmware.vim25.Description;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecFileOperation;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDiskFlatVer2BackingInfo;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualEthernetCardNetworkBackingInfo;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualPCNet32;
import com.vmware.vim25.VirtualSCSISharing;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;


public class AddVM {
	private String abc; 
	String dcName = "Datacenter1";
	String vmName = "vimasterVM" + System.currentTimeMillis();
	long memorySizeMB = 100;
	int cupCount = 1;
	String guestOsId = "sles10Guest";
	long diskSizeKB = 1000000;
	// mode: persistent|independent_persistent,
	// independent_nonpersistent
	String diskMode = "persistent";
	String datastoreName = "datastore1";
	String netName = "VM Network";
	String nicName = "Network Adapter 1";
	private VirtualMachine m_oldVM = null;
	private HostSystem m_newHostSystem = null;
	private HostSystem m_tempHostSystem = null;
	
	
	
	public AddVM(VirtualMachine vm, HostSystem host){
		m_oldVM = vm;
		m_newHostSystem = host;
		initialize();
	}
	
	private void initialize(){
		try {
			Folder rootFolder = AvailabilityManager.si.getRootFolder();
			VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder)
					.searchManagedEntity("VirtualMachine", getOldVM().getName());
			HostSystem hs = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity("HostSystem", getNewHostSystem().getName());
			ManagedEntity mes[] = new InventoryNavigator(rootFolder).searchManagedEntities("Datacenter");
			Datacenter dc = (Datacenter) mes[0];
			dcName = dc.getName();
			ResourcePool rp = (ResourcePool) new InventoryNavigator(dc.getHostFolder()).searchManagedEntities("ResourcePool")[0];
			Folder vmFolder = dc.getVmFolder();

			// create vm config spec
			VirtualMachineConfigSpec vmSpec = new VirtualMachineConfigSpec();
			vmSpec.setName(vmName);
			vmSpec.setAnnotation("VirtualMachine Annotation");
			vmSpec.setMemoryMB(memorySizeMB);
			vmSpec.setNumCPUs(cupCount);
			vmSpec.setGuestId(guestOsId);

			// create virtual devices
			int cKey = 1000;
			VirtualDeviceConfigSpec scsiSpec = createScsiSpec(cKey);
			VirtualDeviceConfigSpec diskSpec = createDiskSpec(datastoreName, cKey, diskSizeKB, diskMode);
			VirtualDeviceConfigSpec nicSpec;
			nicSpec = createNicSpec(netName, nicName);
			vmSpec.setDeviceChange(new VirtualDeviceConfigSpec[] { scsiSpec, diskSpec, nicSpec });
			// create vm file info for the vmx file
			VirtualMachineFileInfo vmfi = new VirtualMachineFileInfo();
			vmfi.setVmPathName("[" + datastoreName + "]");
			vmSpec.setFiles(vmfi);
			try {
				Task task = vmFolder.createVM_Task(vmSpec, rp, hs);
				String result = task.waitForMe();
				if (result == Task.SUCCESS) {
					System.out.println("########### VM Created Sucessfully");
				} else {
					System.out.println("########### VM could not be created. ");
				}
			} catch (Exception e) {
				System.out.println("Error in Creating VM : " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static VirtualDeviceConfigSpec createScsiSpec(int cKey) {
		VirtualDeviceConfigSpec scsiSpec = new VirtualDeviceConfigSpec();
		scsiSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
		VirtualLsiLogicController scsiCtrl = new VirtualLsiLogicController();
		scsiCtrl.setKey(cKey);
		scsiCtrl.setBusNumber(0);
		scsiCtrl.setSharedBus(VirtualSCSISharing.noSharing);
		scsiSpec.setDevice(scsiCtrl);
		return scsiSpec;
	}

	static VirtualDeviceConfigSpec createDiskSpec(String dsName, int cKey,
			long diskSizeKB, String diskMode) {
		VirtualDeviceConfigSpec diskSpec = new VirtualDeviceConfigSpec();
		diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
		diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.create);

		VirtualDisk vd = new VirtualDisk();
		vd.setCapacityInKB(diskSizeKB);
		diskSpec.setDevice(vd);
		vd.setKey(0);
		vd.setUnitNumber(0);
		vd.setControllerKey(cKey);

		VirtualDiskFlatVer2BackingInfo diskfileBacking = new VirtualDiskFlatVer2BackingInfo();
		String fileName = "[" + dsName + "]";
		diskfileBacking.setFileName(fileName);
		diskfileBacking.setDiskMode(diskMode);
		diskfileBacking.setThinProvisioned(true);
		vd.setBacking(diskfileBacking);
		return diskSpec;
	}

	static VirtualDeviceConfigSpec createNicSpec(String netName, String nicName)
			throws Exception {
		VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();
		nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);

		VirtualEthernetCard nic = new VirtualPCNet32();
		VirtualEthernetCardNetworkBackingInfo nicBacking = new VirtualEthernetCardNetworkBackingInfo();
		nicBacking.setDeviceName(netName);

		Description info = new Description();
		info.setLabel(nicName);
		info.setSummary(netName);
		nic.setDeviceInfo(info);

		// type: "generated", "manual", "assigned" by VC
		nic.setAddressType("generated");
		nic.setBacking(nicBacking);
		nic.setKey(0);

		nicSpec.setDevice(nic);
		return nicSpec;
	}
	
	public VirtualMachine getOldVM() {
		return m_oldVM;
	}
	public void setOldVM(VirtualMachine m_oldVM) {
		this.m_oldVM = m_oldVM;
	}

	public HostSystem getNewHostSystem() {
		return m_newHostSystem;
	}

	public void setNewHostSystem(HostSystem m_newHostSystem) {
		this.m_newHostSystem = m_newHostSystem;
	}


}

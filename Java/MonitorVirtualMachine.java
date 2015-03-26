
import com.vmware.vim25.AlarmState;
import com.vmware.vim25.ComputeResourceConfigSpec;
import com.vmware.vim25.HostConnectSpec;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class MonitorVirtualMachine{
	private String m_hostName;
	private String m_hostIP;
	private boolean isRunning = false;
	private VirtualMachine m_currentVM = null;

	public MonitorVirtualMachine(VirtualMachine vm) {
		m_currentVM = vm;
		setHostName(getCurrentVM().getName());
		setHostIP(getCurrentVM().getGuest().getIpAddress());
		monitorVirtualMachineState();
	}
	
	private void monitorVirtualMachineState(){
        System.out.println("\nMonitor VM: " + getHostIP());
		try {
			boolean ping = OtherUtils.ping(getHostIP());
			if (ping) {
				System.out.println("VM" + m_currentVM.getName()+ " is Running: "+ m_currentVM.getGuest().getIpAddress());
				setRunning(true);
			} else {
				System.out.println("VM" + m_currentVM.getName() + "  is NOT Running: " + m_currentVM.getGuest().getIpAddress());
				setRunning(false);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void monitorHostSystem() {
		HostSystem currentHost = null;
		try {
			// Get the HostSystem for the current VM
			currentHost = OtherUtils.getHostSystemForVM(getCurrentVM());
			// Check if the vHost is working properly. Ping vHost
			MonitorHostSystem m = new MonitorHostSystem(currentHost);
			m.setNoOfIterations(3);
			//m.start();
			//m.join();

			if (m.isRunning()) {
				System.out.println("HOST- " + currentHost.getName()
						+ " - is Running\n");
			} else {
				System.out.println("HOST- " + currentHost.getName()
						+ " - is NOT Running\n");
				// If the host is not running then check for alternative host
				HostSystem newActiveHost = OtherUtils
						.getAlternateActiveHost(currentHost);
				if (newActiveHost != null) {
					System.out
					.println("##################  NEW ACTIVE HOST FOUND: "
							+ newActiveHost.getName());
					new AddVM(getCurrentVM(), newActiveHost);

				} else {// else no vHost Found. Then create a new Host and add
					// it to the vcenter.

					System.out
					.println("HOST NOT FOUND: ADD NEW HOST TO VCENTER..");

					if (addNewHostSystem()) {// Successful in adding a new
						// HostSystem
						// add a VM to this host.
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out
			.println("Error in Monitor VM create monitor vHost thread"
					+ e.getMessage());
		}
	}




	private boolean addNewHostSystem() {
		boolean isSuccessfull = false;
		HostConnectSpec hcSpec = new HostConnectSpec();
		hcSpec.setHostName("130.65.132.247");
		hcSpec.setUserName("root");
		hcSpec.setPassword("12!@qwQW");
		ComputeResourceConfigSpec compResSpec = new ComputeResourceConfigSpec();
		Task task = null;
		try {
			ManagedEntity[] dcs = new InventoryNavigator(
					AvailabilityManager.si.getRootFolder())
			.searchManagedEntities("Datacenter");
			/*
			 * Permission permission = new Permission();
			 * permission.setPropagate(true);
			 * permission.setEntity(DisasterRecovery.si.getMOR());
			 */
			task = ((Datacenter) dcs[0]).getDatastoreFolder()
					.addStandaloneHost_Task(hcSpec, compResSpec, true);
			try {
				if (task.waitForMe() == Task.SUCCESS) {
					System.out.println("Host Created Succesfully");
					isSuccessfull = true;
				}
			} catch (Exception e) {
				System.out.println("Error in creating a new vHost2 : "
						+ e.getMessage());
				isSuccessfull = false;
			}
		} catch (Exception e) {
			System.out.println("Error in creating a new vHost : "
					+ e.getMessage());
			isSuccessfull = false;
		}
		return isSuccessfull;
	}
	
/*	@Override
	public void run() {
		String ip = getHostIP();
		String pingResult = "";
		BufferedReader in = null;
		String pingCmd = "ping " + ip;
		try {

				boolean ping = OtherUtils.pingIP(getHostIP());
				if (ping) {
					System.out.println("VM" + m_currentVM.getName()+ " is RUNNING: "+ m_currentVM.getGuest().getIpAddress());
					setRunning(true);
				} else {
					System.out.println("VM" + m_currentVM.getName() + "  is NOT RUNNING: " + m_currentVM.getGuest().getIpAddress());
					setRunning(false);
					// Check if the Host is down
					// monitorHostSystem();
					Thread.sleep(StringConstants.VM_PING_INTERVAL);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally {
				 if (in != null) {
					 try {
						 in.close();
					 } catch (IOException e) {
					 }
				 }
			 }
	}
*/
	
	public boolean checkPowerOffAlarm() {
		AlarmState[] as = getCurrentVM().getTriggeredAlarmState();
		if (as == null)
			return false;
		for (AlarmState state : as) {
			// if the vm has a poweroff alarm, return true;
			
		}
		return false;
	}


	public String getHostName() {
		return m_hostName;
	}

	public void setHostName(String hostName) {
		this.m_hostName = hostName;
	}

	public String getHostIP() {
		return m_hostIP;
	}

	public void setHostIP(String hostIP) {
		this.m_hostIP = hostIP;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public VirtualMachine getCurrentVM() {
		return m_currentVM;
	}

	public void setCurrentVM(VirtualMachine m_currentVM) {
		this.m_currentVM = m_currentVM;
	}

}

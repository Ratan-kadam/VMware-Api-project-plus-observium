
import java.util.Vector;

import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class PerformanceStat extends Thread {
	
	
	public void run(){
		while(true){
			try {
				ManagedEntity[] hosts = new InventoryNavigator(AvailabilityManager.si.getRootFolder()).searchManagedEntities("HostSystem");
				StringBuffer dcBuffer = new StringBuffer();
				
				StringBuffer hostBuffer = null;
				for(int i=0; i<hosts.length; i++){
					hostBuffer = new StringBuffer();
					//System.out.println("host["+i+"]=" + hosts[i].getName());
					hostBuffer.append("\n\n");
					hostBuffer.append("Host Information:" +"host["+i+"]="+ hosts[i].getName());
					
					HostSystem h = (HostSystem) hosts[i];
					if(h != null){
						if(h.getName() != null){
							VirtualMachine vms[] = h.getVms();
							hostBuffer.append("\nTotal VMs: " + vms.length);
							//hostBuffer.append("\n------------------------------");
							StringBuffer vmBuffer = null;
							for (int p = 0; p < vms.length; p++) {
								//VirtualMachineRuntimeInfo vminfo = (VirtualMachineRuntimeInfo) vms[p];
								VirtualMachine vm = (VirtualMachine) vms[p];
								vmBuffer = new StringBuffer();
								if(vm != null)
								{
									//if(vm.getGuest().getIpAddress() != null)
									{
									     
										vmBuffer.append("\n\n-->Virtual Machine " + (p+1));
										vmBuffer.append("\nName: " + vm.getName());
										vmBuffer.append("\nOS: " + vm.getConfig().getGuestFullName());
										vmBuffer.append("\nCPU Number: " + vm.getConfig().getHardware().numCPU);
										vmBuffer.append("\nCPU Usage (max): " + vm.getRuntime().getMaxCpuUsage());
										vmBuffer.append("\nOverall CPU usage: " + vm.getSummary().quickStats.overallCpuUsage + "MHz");
										vmBuffer.append("\nMemory (Max): " + vm.getConfig().getHardware().getMemoryMB() + " MB");
										vmBuffer.append("\nCurrent Memory usage: " + vm.getSummary().quickStats.getHostMemoryUsage()+ " MB");
										vmBuffer.append("\nPower State: " + vm.getRuntime().getPowerState().name());
										vmBuffer.append("\nRunning State: " + vm.getGuest().getGuestState());
										vmBuffer.append("\nIP: " + vm.getGuest().getIpAddress());
										vmBuffer.append("\nClean power off " + vm.getSummary().runtime.cleanPowerOff);
										vmBuffer.append("\nVMTools: " + vm.getGuest().getToolsRunningStatus());
										//vmBuffer.append("\n---------vm.getsumary.quickStat------------------------------------------");
									}
								}
								hostBuffer.append("\n------------------------------");
								if(vmBuffer != null)
									hostBuffer.append(vmBuffer);
							}
						}
					}
					if(hostBuffer != null)
						dcBuffer.append(hostBuffer);
				}
				
				System.out.println("\n\n***** Inventory Statistics *****\n" + dcBuffer);
			} catch (Exception e) {
				System.out.println("Error in SnapShotManager: "+ e.getMessage());
			}

			try{
				Thread.sleep(StringConstants.PERFORMANCE_STAT_INTERVAL * 1000);
			} catch(Exception e){
				System.out.println("Error in creating new Snapshot: " + e.getMessage());
			}
		}
		
	}

}

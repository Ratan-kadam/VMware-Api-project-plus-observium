import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.*;

import javax.management.monitor.Monitor;

import com.vmware.vim25.AlarmSetting;
import com.vmware.vim25.AlarmSpec;
import com.vmware.vim25.DuplicateName;
import com.vmware.vim25.FileFault;
import com.vmware.vim25.InvalidName;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.SnapshotFault;
import com.vmware.vim25.StateAlarmExpression;
import com.vmware.vim25.StateAlarmOperator;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSnapshotInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;
import com.vmware.vim25.mo.samples.CloneThread;
import com.vmware.vim25.mo.samples.StringConstants;
import com.vmware.vim25.mo.samples.vm.VMSnapshot;

public class Run {

	public void totalNumberOfVMS(ServiceInstance sin) 
	{
		//* this api gets number of vms present on Exsi servers ( count Vcenter as well)
		
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0)
			{
				int numberVMS=mes.length - 1;
				System.out.print("Number of total VMS : " + numberVMS ); // removing Vcenter Count from here
			    System.out.print("  + 1 Vcenter is running");
			}
			}
		
		
	}
	
	public void NameAndAddress(ServiceInstance sin) {
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for(int i=0;i<mes.length;i++)
				{
					VirtualMachine vm = (VirtualMachine)mes[i];
					if(vm != null){
					if(vm.getGuest().getIpAddress() != null)
					{   
						System.out.println("\n");
						System.out.print(" | host-Name: " + vm.getName());
						System.out.print(" | IP: " + vm.getGuest().getIpAddress());
						System.out.print(" | Type : " + vm.getGuest().guestFullName);
						//System.out.print(" | host-Name: " + vm.getGuest().getHostName());
					
					}
						}
					}// for completed
				
			}// if completed
		}
	}
	
	public void StopAllVMs(ServiceInstance sin)
	{
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for(int i=0;i<mes.length;i++)
				{
					VirtualMachine vm = (VirtualMachine)  mes[i];
					Task task;
					try {
						task = vm.powerOffVM_Task();
						//task.waitForMe();
					} catch (Exception e) {
						System.out.println("error found.. in catch"); 
						e.printStackTrace();
					}
					
				}
				
			}
		}
		
	}
	
	public void startAllVMs(ServiceInstance sin)
	{
		
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for(int i=0;i<mes.length;i++)
				{
					VirtualMachine vm = (VirtualMachine)  mes[i];
					Task task;
					try {
						//task = vm.powerOffVM_Task();
						task = vm.powerOnVM_Task(null);
						//task.waitForMe();
					} catch (Exception e) {
						System.out.println("error found.. in catch"); 
						e.printStackTrace();
					}
					
				}
				
			}
		}
		
	}
	
	public void stopPerticularVM(ServiceInstance sin)
	{
		Scanner name = new Scanner(System.in);
		System.out.println("Enter the Name of the Machine to be turned off :");
		String vmMachine = name.nextLine();
		//System.out.println(vmMachine);
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for(int i=0;i<mes.length;i++)
				{
					VirtualMachine vm = (VirtualMachine)  mes[i];
					Task task;
					try {
						String vm2 = vm.getName();
						System.out.println("");
						//System.out.print("" + vm2 + " -> " + vmMachine);
						
						
						if (vm2.equalsIgnoreCase(vmMachine) )
						 { 
							
							task = vm.powerOffVM_Task();
							System.out.println(" Machine : " + vmMachine + " is turned off");
							return;
						 }
					
						//task.waitForMe();
					} catch (Exception e) {
						System.out.println("error found.. in catch"); 
						e.printStackTrace();
					}
					
				}
				
			}
		}
		
	}
	
	public void onPerticularVM(ServiceInstance sin)
	{
		Scanner name = new Scanner(System.in);
		System.out.println("Enter the Name of the Machine to be turned on :");
		String vmMachine = name.nextLine();
		//System.out.println(vmMachine);
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for(int i=0;i<mes.length;i++)
				{
					VirtualMachine vm = (VirtualMachine)  mes[i];
					Task task;
					try {
						String vm2 = vm.getName();
						System.out.println("");
						//System.out.print("" + vm2 + " -> " + vmMachine);
						
						
						if (vm2.equalsIgnoreCase(vmMachine) )
						 { 
							
							task = vm.powerOnVM_Task(null);
							System.out.println(" Machine : " + vmMachine + " is turned on");
							return;
						 }
					
						//task.waitForMe();
					} catch (Exception e) {
						System.out.println("error found.. in catch"); 
						e.printStackTrace();
					}
					
				}
				
			}
		}
		
	}
	
	public void pingAllVM(ServiceInstance sin)
	{
		ServiceInstance si = sin;
		AvailabilityManager.si=si;
		String ips = "";
	
					
					if(si !=null)
					{
						ManagedEntity[] mes=null;
						mes = Utility.getAllVirtualMachine();
						if(mes.length > 0)
						{
							for(int i=0;i<mes.length;i++)
							{
								VirtualMachine vm = (VirtualMachine) mes[i];
								ips=vm.getGuest().getIpAddress();
								InetAddress inet = null;
								try {
									inet = InetAddress.getByName(ips);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							    System.out.println("Sending Ping Request to " + ips);
							    try {
									System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
								    System.out.println("");
							    } catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
						
					}
		
		String ipAddress = "192.168.43.132";
	    
		}
	
	public void pingVM(ServiceInstance sin) {
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		Scanner inp = new Scanner(System.in);
		System.out.println("Enter the Machine name :");
		String inpNext = inp.nextLine();

		String ips = "";

		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {
					VirtualMachine vm = (VirtualMachine) mes[i];
					if (vm.getName().equalsIgnoreCase(inpNext)) {
						ips = vm.getGuest().getIpAddress();
						InetAddress inet = null;
						try {
							inet = InetAddress.getByName(ips);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("Sending Ping Request to " + ips);
						try {
							System.out
									.println(inet.isReachable(5000) ? "Host is reachable"
											: "Host is NOT reachable");
							System.out.println("");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else {
						System.out.println(" Host is not reachable");
					}
				}
			}

		}

		

	}
	
	
	public void createSnap(ServiceInstance sin) {
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		String ips = "";
		VirtualMachine vm = null;
		Scanner inp = new Scanner(System.in);
		System.out.println("Enter Machine name ");
		String MachineInp = inp.nextLine();

		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {

				

						// Create a snap shot for a VM
						Thread snapThread = new Thread() {

							public void run() {
								ManagedEntity[] mes = null;
								mes = Utility.getAllVirtualMachine();
								for (int j = 0; j < mes.length; j++) {

								VirtualMachine vm = (VirtualMachine) mes[j];
									if (vm.getName().equals(MachineInp) ) {
										Task taskObj = null;
										int i = 21;

										String description = "backUp_" + vm.getName()
												+ "_" + System.currentTimeMillis();
										try {
										
											System.out
													.println("Creating Snapshot for VM: "
															+ vm.getName()
															+ "---"
															+ vm.getGuest()
																	.getIpAddress());
											i = i + 1;
											String snap_name = vm.getName() + i;
											taskObj = vm.createSnapshot_Task(snap_name,
													description, true, true);
											if (taskObj.waitForMe() == Task.SUCCESS) {
												System.out.println("Snapshot of VM - "
														+ vm.getName() + "- Created");
											}
										} catch (Exception e) {
											System.out
													.println("Error in SnapshotManager - sub-thread: "
															+ e.getMessage());
										}
									}
								}

								
							}
								
						};
						snapThread.start();
							
					

					}
				}
			}
		
	
					
	public void removeSnap(ServiceInstance sin)
	{
		ServiceInstance si = sin; 
		AvailabilityManager.si = sin;
		Scanner input = new Scanner(System.in);
		String inputvm = input.nextLine();
		
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {
					VirtualMachine vm = (VirtualMachine) mes[i];
					if(vm.getName().equals(inputvm))
					{
					   VirtualMachineSnapshotInfo vminfo = vm.getSnapshot();
					   System.out.println("Removing Snapshots process started..");
					   try {
						   
						vm.removeAllSnapshots_Task();
						System.out.println("All Snapshots have been removed..");
						
					} catch (Exception e) {
						System.out.println("SnapShot remove Error encountered..");
					}

					}
				}
			}
		}
		
	}
	
	public void remove_Perticular_snap(ServiceInstance sin)
	{
		ServiceInstance si = sin; 
		AvailabilityManager.si = sin;
		Scanner input = new Scanner(System.in);
		System.out.println("Enter VM name :");
		String inputvm = input.nextLine();
		System.out.println("Enter snapShot name :");
		String inputSnap = input.nextLine();
		
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			
			if (mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {
					VirtualMachine vm = (VirtualMachine) mes[i];
					if(vm.getName().equals(inputvm))
					{
					   try {
						   System.out.println("removing snap..");
						VirtualMachineSnapshot curr_snap = VMSnapshot.getSnapshotInTree(vm,inputSnap);
						if (curr_snap == null) {System.out.println("no snapss");}
						Task task = curr_snap.removeSnapshot_Task(true);
						if(task.waitForMe()==Task.SUCCESS)
										{
											System.out.println( " Snapshot removed");
										}
										else
										{
											System.out.println("Unable to remove snapshot.");  
										}
					   } catch (Exception e) {
						// TODO Auto-generated catch block
					}
					   
					}
				}
			}
		}
		
	}
	
	
	public void SuspendVM(ServiceInstance sin) {
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		Scanner inp = new Scanner(System.in);
		System.out.println("Enter the Machine name to be suspended :");
		String MachineName = inp.nextLine();

		String ips = "";

		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {
					VirtualMachine vm = (VirtualMachine) mes[i];
					if (vm.getName().equalsIgnoreCase(MachineName)) {
						try {
							Task t = vm.suspendVM_Task();
							System.out.println("Machine :" + vm.getName() + " -suspend process started..");
						}

						 catch (Exception e) {
							 System.out.println("Error occured in creating snapshot");
						}
					}

					else {

					}
				}
			}

		}

	}
	
	
	public void SuspendAllVM(ServiceInstance sin) {
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		Scanner inp = new Scanner(System.in);
		

		String ips = "";

		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {
					VirtualMachine vm = (VirtualMachine) mes[i];
					
						try {
							Task t = vm.suspendVM_Task();
							System.out.println("Machine :" + vm.getName() + " -suspend process started..");
						}

						 catch (Exception e) {
							 System.out.println("Error occured in creating snapshot");
						}
				
				}
			}

		}

	}
	
	public void renameVM(ServiceInstance sin)
	{
		
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		Scanner inp = new Scanner(System.in);
		System.out.println("Enter Machine name:");
		String reqName = inp.nextLine();
		String ips = "";
		System.out.println("Enter the New Name :");
	    String newName = inp.nextLine();

		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {
					VirtualMachine vm = (VirtualMachine) mes[i];
					if(vm.getName().equals(reqName))
					{
						
						try {
							Task t = vm.rename_Task(newName);
							System.out.println("Machine Rename process Started ..");
						} catch (Exception e) {
						}
					}
				}
			}
	}
	}
	
	public void cloneVM(String vmName,String cloneName) throws Exception{
		
		VirtualMachine vm1 = null ;
		if (true) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
		
		if (mes.length > 0) {
			for (int i = 0; i < mes.length; i++) {
				VirtualMachine vm = (VirtualMachine) mes[i];
				if(vm.getName().equals(vmName))
				{
					 vm1 = (VirtualMachine) mes[i];
				}
			}
		}
		


		VirtualMachineCloneSpec cloneSpec = 
				new VirtualMachineCloneSpec();
		cloneSpec.setLocation(new VirtualMachineRelocateSpec());
		cloneSpec.setPowerOn(false);
		cloneSpec.setTemplate(false);

		Task task = vm1.cloneVM_Task((Folder) vm1.getParent(), 
				cloneName, cloneSpec);
		System.out.println("Launching the VM clone task. " +
				"Please wait ...");

		@SuppressWarnings("deprecation")
		String status = task.waitForMe();
		if(status==Task.SUCCESS)
		{
			System.out.println("VM got cloned successfully.");
		}
		else
		{
			System.out.println("Failure -: VM cannot be cloned");
		}
	}
}
	
	public static void CloneMachine()
	{
		
	}
	
	public void createAlarm(String vmName, ServiceInstance sin) throws DuplicateName, RuntimeFault, RemoteException {
		
		ServiceInstance si = sin;
		Folder rootFolder = si.getRootFolder();
		ManagedEntity me =  new InventoryNavigator(
				rootFolder).searchManagedEntity(
						"VirtualMachine", vmName);
		
		AlarmManager alarmMgr = si.getAlarmManager();
		//System.out.println(alarmMgr.getAlarmState(me));
		AlarmSpec Spec = new AlarmSpec();

		StateAlarmExpression expression  = createStateAlarmExpression();


		Spec.setExpression(expression);
		Spec.setName("VmPowerStateAlarm"+me.getName());
		Spec.setDescription("Alarm by Team3");
		Spec.setEnabled(true);

		AlarmSetting as = new AlarmSetting();
		as.setReportingFrequency(0); 
		as.setToleranceRange(0);

		Spec.setSetting(as);

		alarmMgr.createAlarm(me, Spec);

	}

	static StateAlarmExpression createStateAlarmExpression() {
		StateAlarmExpression expression
		= new StateAlarmExpression();
		expression.setType("VirtualMachine");
		expression.setStatePath("runtime.powerState");
		expression.setOperator(StateAlarmOperator.isEqual);
		expression.setRed("poweredOff");
		return expression;
	}
	
	
	public void getNumberofHost(ServiceInstance sin)
	{
		ServiceInstance si = sin; 
		AvailabilityManager.si = si;
		
		AvailabilityManager.si = si;
		
		Folder rootFolder= si.getRootFolder();
		
		ManagedEntity[] hosts = null;
		try {
			hosts = new InventoryNavigator(rootFolder).searchManagedEntities(
					new String[][] { {"HostSystem", "name" }, }, true);
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i=0; i<hosts.length; i++)
		{
			System.out.println(  ". " + hosts[i].getName() + "-"+ hosts[i].toString());
		}
		
	}
	
	public void listAllSnaps(ServiceInstance sin)
	{
		ServiceInstance si = sin; 
	   AvailabilityManager.si = si;
	
	Folder rootFolder= si.getRootFolder();
		/*ManagedEntity[] mes = null;
		mes = Utility.getAllVirtualMachine();
		if (mes.length > 0) {
			for (int i = 0; i < mes.length; i++) {
				VirtualMachine vm = (VirtualMachine) mes[i];
				if (vm.getName().equalsIgnoreCase(MachineName)) {
					{*/
				    Scanner inp = new Scanner(System.in);
					System.out.println("Enter the Machine name :");
					String MachineName = inp.nextLine();
								VirtualMachine vm = null;
								try {
									vm = (VirtualMachine) new InventoryNavigator(
									rootFolder).searchManagedEntity("VirtualMachine", MachineName);
								} catch (InvalidProperty e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (RuntimeFault e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								VMSnapshot.listSnapshots(vm);
					}
				//}
			//}
	//	}	
	//}

	public static void main(String[] args) {
		Run run = new Run();
		
		
		ServiceInstance si = Utility.getSiForVCenter();
		AvailabilityManager.si = si;
		
		
		Scanner input = new Scanner(System.in);
		while (true) {
			
			System.out.println("\n\n-----------------------------------------");
			System.out.println("Select below option : ");
			System.out.println("1.Total Number of Virual Machine present on Esxi.");
			System.out.println("2.Get all VMS names and Address");
			System.out.println("3.ShutDown all machines");
			System.out.println("4.On all machines");
			System.out.println("5. Turn off Perticular Machine");
			System.out.println("6. Turn on Perticular Machine");
			System.out.println("7. Ping All Machine");
			System.out.println("8. Ping perticular Machine");
			System.out.println("9.Create SnapShot");
			System.out.println("10. Remove All Snapshots of perticular Machine");
			System.out.println("11. Remove perticular snapshot");
			System.out.println("12.Suspend perticular Machine");
			System.out.println("14.Rename perticulae VmMachine");
			System.out.println("15.Create a new virtual Machine");
			System.out.println("16.Get Performance statistic for all machines on VM ");
			System.out.println("17. Get Number of Host and Details "); 
			System.out.println("18.Revert Snapshot..");
		    System.out.println("19.list all of the snapshots");
		    System.out.println("20. Disaster recovery.");
		    System.out.println("21. Delete Machine.");
			
			
			
			

int choise = input.nextInt();

			switch (choise) {
			case 1:
				System.out.println("Counting the VMS ..");
				run.totalNumberOfVMS(si);
				break;
			case 2:
				run.NameAndAddress(si);
				break;
			case 3: 
				 run.StopAllVMs(si);
				 break;
			case 4: 
				run.startAllVMs(si);
				 break;
			case 5:
			
				run.stopPerticularVM(si);
				break;
			case 6: 
				run.onPerticularVM(si);
				break;
			case 7: 
				run.pingAllVM(si);
				break;
			case 8: 
				run.pingVM(si);
				break;
			case 9:
				run.createSnap(si);
				break;
			case 10: 
				run.removeSnap(si);
				break;
			case 11: 
				run.remove_Perticular_snap(si);
				break;
			case 12:
				run.SuspendVM(si);
				break;
			case 13:
				run.SuspendAllVM(si);
				break;
			case 14: 
				run.renameVM(si);
				break;
			case 15: 
				try {
					VirtualMachine vmInstance1 = Utility.getVirtualMachineFromName("Ratan3");
					HostSystem hostInsstance = Utility.getHostSystemForVM(vmInstance1);
					new AddVM(vmInstance1, hostInsstance);
					
				} catch (Exception e) {
					System.out.println("Error in add the VM: " + e.getMessage());
				}
				break;
			case 16:
				//Performance Stats
				PerformanceStat performanceStat = new PerformanceStat();
				performanceStat.start();
				break;
			/*case 17 : 
				CloneMachine();
				break;
				
			case 18: 
				
				try {
					run.createAlarm("Ratan1", si);
				} catch (Exception  e) {
					System.out.println("no alarm set");
				}	
				break;*/
			case 17:
				run.getNumberofHost(si);
				 break;
			case 18:
				revertsnap rs = new revertsnap();
				rs.revertsnapshot(si);
				
				 break;
			case 19: 
				run.listAllSnaps(si);
				break;
			case 20: 
				DisasterRecoveryThread DRT = new DisasterRecoveryThread();
				DRT.startRecoveringSystems(si);
				break;
			case 21: 
				DeleteVM DVM = new DeleteVM();
				DVM.removeVM(si);
				break;
			      
			}
		}
	}
}
	


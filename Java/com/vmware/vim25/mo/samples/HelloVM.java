/*================================================================================
Copyright (c) 2008 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

* Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior 
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/

package com.vmware.vim25.mo.samples;



import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Scanner;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class HelloVM 
{
	private static ServiceInstance si;
	public static void main(String[] args) throws Exception
	{
		URL url = new URL(StringConstants.VCENTER_URL);
		si = new ServiceInstance(url, StringConstants.VCENTER_USERNAME, StringConstants.VCENTER_PASSWORD, true);
		HelloVM obj = new HelloVM();		
		String ans = "n";

		do{
			System.out.println("\n**************************");
			System.out.println("Choose An Option:");
			System.out.println("**************************");
			System.out.println("1. VM Details");
			System.out.println("2. Power On or Power Off VM");
			System.out.println("3. Clone a VM");
			System.out.println("4. Migrate a VM");
			System.out.println("5. Create a new VM");
			System.out.println("6. Create a new vHost");
			System.out.println("7. Exit\n");
			
			System.out.println("Enter any of the above option number: ");
			Scanner option = new Scanner(System.in);
			int key = option.nextInt();
			switch (key) {
				case 1:
					obj.displayVMDetails();
					break;
	
				case 2:
					obj.powerManageVM();;
					break;
	
				case 3:
					obj.cloneVM();
					break;
	
				case 4:
					obj.migrateVMToNewHost();
					break;

				case 5:
					obj.createNewVM();
					break;
				case 6:
					obj.createNewvHost();
					break;
					
	
				case 7:
					si.getServerConnection().logout();
					System.exit(0);
					break;
	
				default:
					break;
			}
			System.out.println("Do you want to continue? (y|n) : ");
			Scanner sc = new Scanner(System.in);
			ans = sc.nextLine();
		}while(ans.equalsIgnoreCase("y"));
	}
	
	private void createNewVM(){
		
	}
	
	private void createNewvHost(){
		HostConnectSpec hcSpec = new HostConnectSpec();
		hcSpec.setHostName("130.65.132.247");
		hcSpec.setUserName("root");
		hcSpec.setPassword("12!@qwQW");
		//hcSpec.setSslThumbprint("E4:FD:E5:36:B6:D7:D3:F7:82:A5:39:0E:F2:83:CC:7E:3F:DB:26:74");
		hcSpec.setForce(true);
		ComputeResourceConfigSpec compResSpec = new ComputeResourceConfigSpec();
		Task task  = null;
		try {
			ManagedEntity[] dcs = new InventoryNavigator(si.getRootFolder()).searchManagedEntities("Datacenter");
			System.out.println("Data: "+ dcs[0].getName());
			task = ((Datacenter)dcs[0]).getHostFolder().addStandaloneHost_Task(hcSpec, compResSpec, true);
			try {
				if(task.waitForMe() == Task.SUCCESS){
					System.out.println("Host Created Succesfully..............................");
				}else{
					System.out.println("HOST NOT CREATED............");
				}
			} catch (Exception e) {
				System.out.println("Error in vHost creating wait for me: " + e.getMessage());
			} 
		} catch (Exception e) {
			System.out.println("Error in creating a new vHost.. : " + e.getMessage());
		}

	}
	
	private void displayVMDetails(){
		//Enter the VM name
		String vName = enterVMName();
		if(vName != null || vName != ""){
			VirtualMachine vm = getVMInstance(vName);
			if(vm == null){
				return;
			}
			VirtualMachineConfigInfo vminfo = vm.getConfig();
			VirtualMachineCapability vmc = vm.getCapability();

			/*
			 * try { vm.getResourcePool(); } catch (Exception e) {
			 * e.printStackTrace(); }
			 */
			try {
				for (int i = 0; i < vm.getDatastores().length; i++) {
					Datastore d = vm.getDatastores()[i];
					System.out.println("VM datastore" + d.getName());
				}
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage());
			}
			
			System.out.println("Power State: " + vm.getRuntime().getPowerState());
			System.out.println("Connection State: " + vm.getRuntime().getConnectionState());
			System.out.println("Hello " + vm.getName());
			System.out.println("GuestOS: " + vminfo.getGuestFullName());
			System.out.println("IP Address: " + vm.getGuest().getIpAddress());
			System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());
			System.out.println("Parent Folder: " + vm.getParent().getName());
			System.out.println("------------------------\n");

		}
		
	}

	private void powerManageVM(){
		//Enter the VM name you need to power ON or OFF
		String vName = enterVMName();
		if(vName != null |vName != ""){
			VirtualMachine vm = getVMInstance(vName);
			if(vm == null){
				return;
			}
			VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
			//System.out.println(summary.toString());
			VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
			
			//System.out.println("Power State - " + vmri.getPowerState());
			if(vmri.getPowerState() == VirtualMachinePowerState.poweredOff){
				//VM is OFF, do you want to switch ON?
				System.out.println("VM is OFF, do you want to switch ON? y or n? : ");
				Scanner scr = new Scanner(System.in);
				String ans = scr.nextLine();
				if (ans.toLowerCase().equals("y")) {
					try {
						Task task = vm.powerOnVM_Task(null);
						task.waitForMe();
						System.out.println("VM: " + vm.getName() + " is now powered ON.\n");
					} catch (Exception e) {
						System.out.println("Error in powerManage2: " + e.getMessage());
					}
				}
			}else if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn){
				//VM is ON, do you want to switch it OFF?
				System.out.println("VM is ON, do you want to switch OFF? y or n? : ");
				Scanner scr = new Scanner(System.in);
				String ans = scr.nextLine();
				if(ans.toLowerCase().equals("y")){
					try {
						Task task = vm.powerOffVM_Task();
						task.waitForMe();
						System.out.println("vm:" + vm.getName() + "powered OFF.");
					} catch (Exception e) {
						System.out.println("Error in powerManage1: " + e.getMessage());
					}
				}
				
			}
		}
	}
	
	private void cloneVM(){
		//Enter a VM to clone
		String vName = enterVMName();
		VirtualMachine vm = null;
		if(vName != null |vName != ""){
			vm = getVMInstance(vName);
			if(vm == null)
				return;
		}
		//Enter the new VM name for the clonned VM
		System.out.println("Enter the name of clone:");
		Scanner scr = new Scanner(System.in);
		String cloneName = scr.nextLine();
		if (cloneName != null || cloneName != "") {
			try{
				//ManagedObjectReference newHost = (HostSystem) new InventoryNavigator( si.getRootFolder()).searchManagedEntity("HostSystem", "130.65.132.242");
				
				VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
				VirtualMachineRelocateSpec  recSpec = new VirtualMachineRelocateSpec();
				//newHost.getDatastores();
				//recSpec.setHost(newHost);
				cloneSpec.setLocation(new VirtualMachineRelocateSpec());
				cloneSpec.setPowerOn(false);
				cloneSpec.setTemplate(false);
				//cloneSpec.setLocation(newHost);
				try {
					//HostSystem newHost = (HostSystem) new InventoryNavigator( si.getRootFolder()).searchManagedEntity("HostSystem", newHostName);

					System.out.println("Launching the VM clone task. " + "Please wait ...");
					CloneThread cloneThread = new CloneThread();
					cloneThread.setVm(vm);
					cloneThread.setCloneName(cloneName);
					cloneThread.setCloneSpec(cloneSpec);
					new Thread(cloneThread).start();
				} catch (Exception e) {
					System.out.println("Error in cloneVM: " + e.getMessage());
				}
	
			}catch(Exception e){
				System.out.println("Error in CloneVM: "+ e.getMessage());
			}
		}
	}
	
	private void migrateVMToNewHost(){
		//Enter the VM name you want to migrate
		String vName = enterVMName();
		VirtualMachine vm = null;
		if(vName != null |vName != ""){
			vm = getVMInstance(vName);
			if(vm == null)
				return;
		}
		System.out.println("Enter the name of host:");
		Scanner scr = new Scanner(System.in);
		String newHostName = scr.nextLine();
		if (newHostName != null || newHostName != "") {
			try {
				HostSystem newHost = (HostSystem) new InventoryNavigator( si.getRootFolder()).searchManagedEntity("HostSystem", newHostName);
				ComputeResource cr = (ComputeResource) newHost.getParent();

				String[] checks = new String[] { "cpu", "software" };
				HostVMotionCompatibility[] vmcs = si.queryVMotionCompatibility(vm, new HostSystem[] { newHost }, checks);

				String[] comps = vmcs[0].getCompatibility();
				if (checks.length != comps.length) {
					System.out.println("CPU/software NOT compatible. Exit.");
					//si.getServerConnection().logout();
					return;
				}
				if(vm.getRuntime().getPowerState() == VirtualMachinePowerState.poweredOn){
					//Switch off the machine, so that it is ready for cold migration.
					try {
						Task task = vm.powerOffVM_Task();
						task.waitForMe();
						System.out.println("vm:" + vm.getName() + "powered OFF.");
					} catch (Exception e) {
						System.out.println("Error in migrate power off: " + e.getMessage());
					}
				}

				Task task = vm.migrateVM_Task(cr.getResourcePool(), newHost, VirtualMachineMovePriority.highPriority, VirtualMachinePowerState.poweredOff);

				if (task.waitForMe() == Task.SUCCESS) {
					System.out.println("VMotioned!");
				} else {
					System.out.println("VMotion failed!");
					TaskInfo info = task.getTaskInfo();
					System.out.println(info.getError().getFault());
				}
			} catch (Exception e) {
				System.out.println("Exception in Migrate VM: " + e.getMessage());
			}
		}
		//Enter the new host you would like to migrate it to.
		//DO a Cold Migration
		//Check if the system if On | Off
		//If On, then switch it off and then migrate 
		//else migrate it
		
	}
	
	private VirtualMachine getVMInstance(String vmName){
		//Traverse all the instances and return the required VM
		Folder rootFolder = si.getRootFolder();
		//String name = rootFolder.getName();
		//System.out.println("root:" + name);
		ManagedEntity[] mes = null;
		ManagedEntity[] mes1 = null;
		
		try {
			mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
			
			mes1 = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
			for(int i = 0; i < mes1.length; i++){
				VirtualMachine vm =  (VirtualMachine) mes[i];
				System.out.println("Host System ip address: " + vm.getGuest().getIpAddress());
			}
			if(mes==null || mes.length ==0){
				return null;
			}
			for(int i = 0; i < mes.length; i++){
				VirtualMachine vm =  (VirtualMachine) mes[i];
				System.out.println("VM ip address: " + vm.getGuest().getIpAddress());
				if(vm.getName().toLowerCase().equals(vmName.toLowerCase())){
					//Return the VM object
					return vm;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String enterVMName(){
		//Enter the VM Name
		String vmName = null;
		System.out.println("Enter the VM name:");
		Scanner src = new Scanner(System.in);
		vmName = src.nextLine();
		return vmName;
	}

}

package com.vmware.vim25.mo.samples;

import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class CloneThread implements Runnable {
	private VirtualMachine vm;
	private String cloneName;
	private VirtualMachineCloneSpec cloneSpec;
	private Folder destinationHost;
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Folder a = getDestinationHost();
			Task task = vm.cloneVM_Task(a, cloneName, cloneSpec);
			String status = task.waitForMe();
			if (status == Task.SUCCESS) {
				System.out.println("VM got cloned successfully.");
			} else {
				System.out.println("Failure -: VM cannot be cloned");
			}
		} catch (Exception e) {
			System.out.println("Error in clonethread: " + e.getMessage());
		}

	}

	public VirtualMachineCloneSpec getCloneSpec() {
		return cloneSpec;
	}

	public void setCloneSpec(VirtualMachineCloneSpec cloneSpec) {
		this.cloneSpec = cloneSpec;
	}

	public String getCloneName() {
		return cloneName;
	}

	public void setCloneName(String cloneName) {
		this.cloneName = cloneName;
	}

	public VirtualMachine getVm() {
		return vm;
	}

	public void setVm(VirtualMachine vm) {
		this.vm = vm;
	}

	public Folder getDestinationHost() {
		return destinationHost;
	}

	public void setDestinationHost(Folder destinationHost) {
		this.destinationHost = destinationHost;
	}

}

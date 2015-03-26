import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;


public class startAllVirtualMachines {

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
}

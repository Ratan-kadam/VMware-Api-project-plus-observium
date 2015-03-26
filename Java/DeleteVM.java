import java.rmi.RemoteException;
import java.util.Scanner;

import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.VimFault;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;


public class DeleteVM {
	
	public void removeVM(ServiceInstance sin) {
		ServiceInstance si = sin;
		AvailabilityManager.si = si;
		Scanner inp = new Scanner(System.in);
		System.out.println("Enter the Machine name tobe deleted: ");
		String VMname = inp.nextLine();
		if (si != null) {
			ManagedEntity[] mes = null;
			mes = Utility.getAllVirtualMachine();
			if (mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {

					VirtualMachine vm = (VirtualMachine) mes[i];
					if (vm.getName().equals(VMname)) {
						System.out
								.println("Are you sure you want to delete vm : "
										+ vm.getName());
						String Decision = inp.nextLine();
						if (Decision.equalsIgnoreCase("yes")) {
							try {
								Task task = vm.destroy_Task();
							} catch (VimFault e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RuntimeFault e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							System.out
									.println(" VMware deleting process started..");
						}
						else
						{
							System.out.println("Deleting process terminated..");
						}
					}

				}
			}
		}
	}
}
	
	


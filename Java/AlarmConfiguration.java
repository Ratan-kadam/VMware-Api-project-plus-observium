import com.vmware.vim25.Action;
import com.vmware.vim25.AlarmAction;
import com.vmware.vim25.AlarmInfo;
import com.vmware.vim25.AlarmSetting;
import com.vmware.vim25.AlarmSpec;
import com.vmware.vim25.AlarmTriggeringAction;
import com.vmware.vim25.GroupAlarmAction;
import com.vmware.vim25.MethodAction;
import com.vmware.vim25.MethodActionArgument;
import com.vmware.vim25.SendEmailAction;
import com.vmware.vim25.StateAlarmExpression;
import com.vmware.vim25.StateAlarmOperator;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.VirtualMachine;


public class AlarmConfiguration {
	private VirtualMachine m_vm;
	private AlarmSpec m_alarmSpecification;
	private AlarmSetting m_alarmSetting;
	
	
	public AlarmConfiguration(VirtualMachine vm){
		if(vm != null){
			setVM(vm);
		}
		//check if the vm has previous alarm set
		//remove previous alarm if possible
		//create an alarm
		try {
			AlarmManager alarmManager = DisasterRecovery.si.getAlarmManager();
			alarmManager.createAlarm(getVM(), getAlarmSpecifications());
		} catch (Exception e) {
			System.out.println("Error in AlarmConfiguration " + e.getMessage());
		}
	}
	
	//alarm settings
	private AlarmSetting getAlarmSettings(){
		m_alarmSetting = new AlarmSetting();
		m_alarmSetting.setReportingFrequency(0);
		m_alarmSetting.setToleranceRange(0);
		return m_alarmSetting;
	}
	
	//alarm specifications
	private AlarmSpec getAlarmSpecifications(){
		StateAlarmExpression expression = createStateAlarmExpression();
		m_alarmSpecification = new AlarmSpec();
		m_alarmSpecification.setAction(getGroupAlarmAction());
		m_alarmSpecification.setExpression(expression);
		m_alarmSpecification.setSetting(getAlarmSettings());
		m_alarmSpecification.setName("VmPowerStateAlarm"+ System.currentTimeMillis());
		m_alarmSpecification.setDescription("Monitor VM state and send email and power it on if VM powers off");
		m_alarmSpecification.setEnabled(true);
		return m_alarmSpecification;
	}
	
	//get group Alarm Action
	public GroupAlarmAction getGroupAlarmAction(){
		AlarmAction emailAction = createAlarmTriggerAction(createEmailAction());
		AlarmAction methodAction = createAlarmTriggerAction(createPowerOnAction());
		GroupAlarmAction gaa = new GroupAlarmAction();
		//gaa.setAction(new AlarmAction[] { emailAction, methodAction });
		gaa.setAction(new AlarmAction[] { methodAction });
		return gaa;
	}
	
	static StateAlarmExpression createStateAlarmExpression() {
		StateAlarmExpression expression = new StateAlarmExpression();
		expression.setType("VirtualMachine");
		expression.setStatePath("runtime.powerState");
		expression.setOperator(StateAlarmOperator.isEqual);
		expression.setRed("poweredOff");
		return expression;
	}

	static MethodAction createPowerOnAction() {
		MethodAction action = new MethodAction();
		action.setName("PowerOnVM_Task");
		MethodActionArgument argument = new MethodActionArgument();
		argument.setValue(null);
		action.setArgument(new MethodActionArgument[] { argument });
		return action;
	}

	static SendEmailAction createEmailAction() {
		SendEmailAction action = new SendEmailAction();
		action.setToList("sjin@vmware.com");
		action.setCcList("admins99999@vmware.com");
		action.setSubject("Alarm - {alarmName} on {targetName}\n");
		action.setBody("Description:{eventDescription}\n"
				+ "TriggeringSummary:{triggeringSummary}\n"
				+ "newStatus:{newStatus}\n" + "oldStatus:{oldStatus}\n"
				+ "target:{target}");
		return action;
	}

	static AlarmTriggeringAction createAlarmTriggerAction(Action action) {
		AlarmTriggeringAction alarmAction = new AlarmTriggeringAction();
		alarmAction.setYellow2red(true);
		alarmAction.setAction(action);
		return alarmAction;
	}
	
	public VirtualMachine getVM() {
		return m_vm;
	}

	public void setVM(VirtualMachine vm) {
		this.m_vm = vm;
	}

}

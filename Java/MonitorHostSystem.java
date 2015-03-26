
import com.vmware.vim25.mo.HostSystem;


public class MonitorHostSystem{
	private boolean isRunning = false;
	private int m_flag = 0;
	private int m_noOfIterations = 1;

	private HostSystem m_host;
	
	public MonitorHostSystem(HostSystem host){
		m_host = host;
		setHostIP(host.getName());
		setHostName(host.getName());
		monitorHostSystemState();
	}

	public void monitorHostSystemState(){
        System.out.println("\nMonitor Host: " + getHostIP());
        while(m_flag < m_noOfIterations){
            try {
				boolean ping = OtherUtils.ping(getHostIP());
                if(ping){
                	System.out.println("HOST" + getHostName() + "  is Running....\n");
                	setRunning(true);
                }else {
                	System.out.println("HOST" + getHostName() + "  is NOT Running.....\n");
                	setRunning(false);
                }
                m_flag++;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
	}

	public String getHostName() {
		return getHost().getName();
	}

	public void setHostName(String hostName) {
	}

	public String getHostIP() {
		return getHost().getName();
	}

	public void setHostIP(String hostIP) {
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public HostSystem getHost() {
		return m_host;
	}
	

	public void setHost(HostSystem m_host) {
		this.m_host = m_host;
	}

	public int getNoOfIterations() {
		return m_noOfIterations;
	}

	public void setNoOfIterations(int m_noOfIterations) {
		this.m_noOfIterations = m_noOfIterations;
	}

	
}

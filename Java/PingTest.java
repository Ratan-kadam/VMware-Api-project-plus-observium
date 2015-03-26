import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class PingTest {
	public static void main(String[] args) {
        String ip = "130.65.133.243";
        String pingResult = "";

        String pingCmd = "ping " + ip;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingCmd);

            BufferedReader in = new BufferedReader(new
            InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                pingResult += inputLine;
            }
            in.close();
            if(pingResult.toLowerCase().contains("reply")){
            	System.out.println("VM is running");
            }else{
            	System.out.println("VM is not working.....");
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}

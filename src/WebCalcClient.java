/**
 * 
 */
import java.net.*;
import java.io.*;

/**
 * @author Michael W. Havens
 *
 */
public class WebCalcClient {
	private int serverport = 8999;
	private String serverip = "127.0.0.1";
	private Socket socket = null;
	private BufferedReader in = null;
    private PrintWriter out = null;
    
    public WebCalcClient() {
    }
    
	public WebCalcClient(int servport) {
		serverport = servport;
	}
	
	public WebCalcClient(String servip) {
		serverip = servip;
	}
	
	public WebCalcClient(String servip, int servport){
		serverport = servport;
		serverip = servip;
	}
	
	public String connect() throws UnknownHostException, IOException{
		socket = new Socket(serverip, serverport);
		in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		String servOutput = socketRead(in);
		System.out.println("Client GOT: '" + servOutput + "' From Server!");
		return servOutput;
	}
	
	public String Write(String toSend) throws IOException{
		String servOutput = "";
		if (socket != null){
			out.println(toSend);
			servOutput = socketRead(in);
		}
		return servOutput;
	}
	
	private String socketRead(BufferedReader sockIn) throws IOException{
		String servOutput = "";
		String serverResponse = null;
		do{
			serverResponse = sockIn.readLine();
			servOutput = servOutput + "\n"+ serverResponse;
		} while( serverResponse != null && ! serverResponse.equals(""));
		return servOutput;
	}
	
}

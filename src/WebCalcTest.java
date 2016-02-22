import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
/**
 * 
 */

/**
 * @author Michael Havens
 * @description 
 */
public class WebCalcTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebCalcServer testServer = new WebCalcServer();
		testServer.start();
		System.out.println("Test Started Server!");
		Scanner scanner = new Scanner(System.in);
		WebCalcClient testClient = new WebCalcClient();
		try {
			String servResp = "";
			servResp = testClient.connect();
			System.out.println("TEST: " + servResp);
			servResp = testClient.Write("1+1");
			System.out.println("Test 1+1 = " + servResp);
			String expr = scanner.next();
			while (! expr.equals(".")){
				System.out.println("expr.equals = " + expr.equals("."));
				servResp = testClient.Write(expr);
				System.out.println("TEST: " + servResp);
				expr = scanner.next();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

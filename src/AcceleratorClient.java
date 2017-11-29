import java.net.ServerSocket;
import java.net.Socket;

public class AcceleratorClient {
	
	public class socketThread extends Thread {
			
			Socket socket = null;
			
			public void createSocket() {
				try {
					socket = new Socket();
				}
				catch (Exception e) {
					System.out.println("Error creating ServerSocket in thread " + this.getName());
				}
	
			}
	}
}

import java.net.ServerSocket;
import java.net.Socket;

public class AcceleratorClient {
	
	public void createThreads() {
		SocketThread socketThread1 = new SocketThread(1515);
		SocketThread socketThread2 = new SocketThread(1616);
		SocketThread socketThread3 = new SocketThread(1717);
		SocketThread socketThread4 = new SocketThread(1818);
		SocketThread socketThread5 = new SocketThread(1919);
	}
	
	public class SocketThread extends Thread {
			
			Socket socket = null;
			
			public SocketThread(int portNum) {
				try {
					socket = new Socket("localHost", portNum);
					System.out.println(this.getName()+ " is Connected.");
				}
				catch (Exception e) {
					System.out.println("Error creating ServerSocket in thread " + this.getName());
				}
			}
	}
}

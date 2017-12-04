import java.net.ServerSocket;
import java.net.Socket;

public class AcceleratorClient {
	
	public void createThreads() {
		SocketThread socketThread1 = new SocketThread(55556);
		SocketThread socketThread2 = new SocketThread(55557);
		SocketThread socketThread3 = new SocketThread(55558);
		SocketThread socketThread4 = new SocketThread(55559);
		SocketThread socketThread5 = new SocketThread(55560);
	}
	
	public class SocketThread extends Thread {
			
			private Socket socket = null;
			private int portNum = 0;
			
			
			public SocketThread(int tempPort) {
				portNum = tempPort;
				start();  // Goes to run()
			}
			
			public void run() {
				try {
					socket = new Socket("localHost", portNum);
					System.out.println("Thread: " + portNum + " is Connected.");
				}
				catch (Exception e) {
					System.out.println("Error creating ServerSocket in thread " + this.getName());
				}
			}
	}
	
	public static void main(String[] args) {
		AcceleratorClient acceleratorClient = new AcceleratorClient();
		acceleratorClient.createThreads();
	}
}

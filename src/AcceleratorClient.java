import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.*;

public class AcceleratorClient {
	
	public void createThreads() {
		SocketThread socketThread1 = new SocketThread(55556);
		SocketThread socketThread2 = new SocketThread(55557);
		SocketThread socketThread3 = new SocketThread(55558);
		SocketThread socketThread4 = new SocketThread(55559);
		SocketThread socketThread5 = new SocketThread(55560);
	}
	
	public class SocketThread extends Thread {
			
	    private DataInputStream inputStream = null;
//	    private OutputStream outputStream = null;
	    
		private Socket socket = null;
		private int portNum;
	    private FileOutputStream outputFile = null;
		
		public SocketThread(int tempPort) {
			portNum = tempPort;
			start();  // Goes to run()
		}
		
		public void run() {
			try {
				socket = new Socket("localHost", portNum);
				System.out.println("Thread: " + portNum + " is Connected.");
				inputStream = new DataInputStream(socket.getInputStream());
				outputFile = new FileOutputStream("D:\\Computer Science\\CS 260\\DownloadAccelerator\\output"
													+ portNum + ".txt", true);
				System.out.println("Output file created in Thread: " + portNum);
			}
			catch (Exception e) {
				System.out.println("Error creating OutputStream in Thread :" + portNum);
			}

			while(true) {
				try {
				    byte[] byteArray = new byte[1000];
					int numBytes = inputStream.read(byteArray, 0, 1000);
					System.out.println("Success!");
					byte[] buffer = new byte[numBytes];
					System.out.println("Input read from inputStream in Thread: " + portNum);
					outputFile.write(buffer);
					System.out.println("Output written to outputFile in Thread: " + portNum);
					if (numBytes < 1000) {
						break;
					}
				}
				catch (Exception e) {
					System.out.println("Error creating receiving data from server in Thread: " + portNum);
				}
			}
		}
		
		
	}
	
	public static void main(String[] args) {
		AcceleratorClient acceleratorClient = new AcceleratorClient();
		acceleratorClient.createThreads();
	}
}

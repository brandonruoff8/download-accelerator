import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.*;

public class AcceleratorServer {
	
	private FileDivider fileDivider = new FileDivider();
	private DataOutputStream outputStream = null;
//  private InputStream inputStream = null;
    private File file = null;
	String fileName = null;
	
	public AcceleratorServer() {
		fileName = "input.txt";
	}
	
	public AcceleratorServer(String inputFileName) {
		fileName = inputFileName;
	}
	
	
	public void divideFile() {
		fileDivider.divideFile(fileName, "D:\\Computer Science\\CS 260\\DownloadAccelerator\\");
	}
	
	public void createThreads() {
		serverSocketThread serverThread1 = new serverSocketThread(55556);
		serverSocketThread serverThread2 = new serverSocketThread(55557);
		serverSocketThread serverThread3 = new serverSocketThread(55558);
		serverSocketThread serverThread4 = new serverSocketThread(55559);
		serverSocketThread serverThread5 = new serverSocketThread(55560);		
	}
	
	public class serverSocketThread extends Thread {
		
		private ServerSocket serverSocket = null;
	    private Socket socket = null;
	    private FileInputStream partition = null;
	    private int portNum = 0;
	    
	    public serverSocketThread(int tempPort) {
	    	portNum = tempPort;
	    	start();  // Goes to run()
	    }
	    
	    public void run() {
			try {
				serverSocket = new ServerSocket(portNum);
				portNum = serverSocket.getLocalPort();
				System.out.println("ServerSocket objects created in Thread: " + portNum 
									+ ". Waiting for client to connect...");
				socket = serverSocket.accept();
				System.out.println("Connected to client in Thread: " + portNum);
				outputStream = new DataOutputStream(socket.getOutputStream());
				file = new File("D:\\Computer Science\\CS 260\\DownloadAccelerator\\output" 
						+ portNum
						+ ".txt");
				partition = new FileInputStream(file);
				System.out.println("Now sending partition to client in Thread: " + portNum);
				writeToClient();
			}
			catch (Exception e) {
				System.out.println("Error creating ServerSocket in Thread: " + portNum);
			}
	    }
	    
	    public void writeToClient() {
	    	try {
		    	byte[] buffer = new byte[(int)partition.getChannel().size()];
		    	partition.read(buffer);
		    	outputStream.write(buffer);
	    	}
	    	catch(Exception e) {
	    		System.out.println("Error occured while sending partition from " + this.getName());
	    	}
	    }
	}
	
	public class FileDivider {
		
		public void divideFile(String fileName, String directory) {
			try {
				FileOutputStream file1 = new FileOutputStream(directory + "inputPort1.txt");
				FileOutputStream file2 = new FileOutputStream(directory + "inputPort2.txt"); 
				FileOutputStream file3 = new FileOutputStream(directory + "inputPort3.txt");
				FileOutputStream file4 = new FileOutputStream(directory + "inputPort4.txt");
				FileOutputStream file5 = new FileOutputStream(directory + "inputPort5.txt");
				FileInputStream inputFile = new FileInputStream(directory + fileName);
				int fileSize = (int)inputFile.getChannel().size();
				byte[] buffer = new byte[fileSize/5];
				
				inputFile.read(buffer);
				file1.write(buffer);

				inputFile.read(buffer);
				file2.write(buffer);
				
				inputFile.read(buffer);
				file3.write(buffer);
				
				inputFile.read(buffer);
				file4.write(buffer);
				
				inputFile.read(buffer);
				file5.write(buffer);
				
				file1.close();
				file2.close();
				file3.close();
				file4.close();
				file5.close();
				inputFile.close();
			}
			catch (Exception e) {
				System.out.print("Error occured while making or writing to files.");
				System.exit(0);
			}

		}
	}
	
	public static void main(String[] args) {
		AcceleratorServer acceleratorServer = new AcceleratorServer("input.txt");
		acceleratorServer.createThreads();
		System.out.println("Threads created.");
		System.out.println("Dividing input file...");
		acceleratorServer.divideFile();
		System.out.println("File division completed.");

	}
}

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class AcceleratorServer {
	
	private FileDivider fileDivider = new FileDivider();
	private OutputStream outputStream = null;
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
	    private File partition = null;
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
				outputStream = socket.getOutputStream();
				partition = new File("D:\\Computer Science\\CS 260\\DownloadAccelerator\\" 
									+ portNum
									+ ".txt");
			}
			catch (Exception e) {
				System.out.println("Error creating ServerSocket in Thread: " + portNum);
			}
	    }
	    
	    public void writeToClient() {
	    	
	    }
	}
	
	public class FileDivider {
		
		public void divideFile(String fileName, String directory) {
			try {
				FileOutputStream file1 = new FileOutputStream(directory + "port1.txt");
				FileOutputStream file2 = new FileOutputStream(directory + "port2.txt"); 
				FileOutputStream file3 = new FileOutputStream(directory + "port3.txt");
				FileOutputStream file4 = new FileOutputStream(directory + "port4.txt");
				FileOutputStream file5 = new FileOutputStream(directory + "port5.txt");
				FileInputStream inputFile = new FileInputStream(directory + fileName);
				double fileSize = inputFile.getChannel().size();
				
				int fileIndex = 0;
				while(fileIndex < (fileSize/5)) {
					file1.write(inputFile.read());
					fileIndex++;
				}
				while(fileIndex < 2*(fileSize/5)) {
					file2.write(inputFile.read());
					fileIndex++;
				}
				while(fileIndex < 3*(fileSize/5)) {
					file3.write(inputFile.read());
					fileIndex++;
				}
				while(fileIndex < 4*(fileSize/5)) {
					file4.write(inputFile.read());
					fileIndex++;
				}
				while(fileIndex < (fileSize)) {
					file5.write(inputFile.read());
					fileIndex++;
				}
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
		System.out.println("Dividing input file...");
		acceleratorServer.divideFile();
		System.out.println("Completed.");
		acceleratorServer.createThreads();
	}
}

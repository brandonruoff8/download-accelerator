import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.*;

public class AcceleratorServer {
	
	private DataOutputStream outputStream = null;
//  private InputStream inputStream = null;
    private File file = null;
	String fileName = null;
	String directory = null;
    public boolean doneDividing = false;
    
    public ServerSocketThread serverThread1;
    public ServerSocketThread serverThread2;
    public ServerSocketThread serverThread3;
    public ServerSocketThread serverThread4;
    public ServerSocketThread serverThread5;
	
	public AcceleratorServer() {
		fileName = "input.txt";
		directory = "C:\\Users\\brand\\Documents\\Computer Science\\CS 260\\DownloadAccelerator\\";
	}
	
	public AcceleratorServer(String inputFileName) {
		fileName = inputFileName;
		directory = "C:\\Users\\brand\\Documents\\Computer Science\\CS 260\\DownloadAccelerator\\";
	}
	
	public AcceleratorServer(String inputFileName, String inputDirectory) {
		fileName = inputFileName;
		directory = inputDirectory;
	}
	
	public void createThreads() {
		serverThread1 = new ServerSocketThread(55556);
		serverThread2 = new ServerSocketThread(55557);
		serverThread3 = new ServerSocketThread(55558);
		serverThread4 = new ServerSocketThread(55559);
		serverThread5 = new ServerSocketThread(55560);	
		serverThread1.start();
		serverThread2.start();
		serverThread3.start();
		serverThread4.start();
		serverThread5.start();
	}
	
	public synchronized void notifyThreads() {
			doneDividing = true;
			serverThread1.notify();
			serverThread2.notify();
			serverThread3.notify();
			serverThread4.notify();
			serverThread5.notify();	
	}
	
	public void divideFile() {
		try {
			FileOutputStream file1 = new FileOutputStream(directory + "inputPort1.txt");
			FileOutputStream file2 = new FileOutputStream(directory + "inputPort2.txt"); 
			FileOutputStream file3 = new FileOutputStream(directory + "inputPort3.txt");
			FileOutputStream file4 = new FileOutputStream(directory + "inputPort4.txt");
			FileOutputStream file5 = new FileOutputStream(directory + "inputPort5.txt");
			FileInputStream inputFile = new FileInputStream(directory + fileName);
//			int fileSize = (int)inputFile.getChannel().size();
			byte[] buffer = new byte[10000];
			
			divideWrite(inputFile, file1, buffer);
			divideWrite(inputFile, file2, buffer);
			divideWrite(inputFile, file3, buffer);
			divideWrite(inputFile, file4, buffer);
			divideWrite(inputFile, file5, buffer);
			
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
	
	private void divideWrite(FileInputStream inputFile, FileOutputStream file, byte[] buffer) {
		try {
			long fileSize = (inputFile.getChannel().size())/5;
			long remainingBytes = fileSize;
			while (true) {	
				if (remainingBytes > 10000) {
					inputFile.read(buffer);
					file.write(buffer);
					remainingBytes = remainingBytes - 10000;
				}
				else {
					inputFile.read(buffer, 0, (int)remainingBytes);
					file.write(buffer);
					break;
				}

			}
		}
		catch (Exception e) {
			System.out.println("Error occurred in divideWrite function.");
		}
	}
	
	public class ServerSocketThread extends Thread {
		
		private ServerSocket serverSocket = null;
	    private Socket socket = null;
	    private FileInputStream partition = null;
	    private int portNum = 0;
	    
	    public ServerSocketThread(int tempPort) {
	    	portNum = tempPort;
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
				file = new File("C:\\Users\\brand\\Documents\\Computer Science\\CS 260\\DownloadAccelerator\\output" 
						+ portNum
						+ ".txt");
				partition = new FileInputStream(file);
				
				writeToClient();
			}
			catch (Exception e) {
				System.out.println("Error creating ServerSocket in Thread: " + portNum);
			}
	    }
	    
	    public void writeToClient() {
	    	try {
				System.out.println("Now sending partition to client in Thread: " + portNum);
		    	byte[] buffer = new byte[10000];
		    	while(true) {
			    	int numBytes = partition.read(buffer);
			    	outputStream.write(buffer);
			    	outputStream.flush();
			    	if(numBytes < 10000) {
			    		break;
			    	}
		    	}
	    	}
	    	catch(Exception e) {
	    		System.out.println("Error occured while sending partition from " + this.getName());
	    	}
	    }
	}
	
	public static void main(String[] args) {
		AcceleratorServer acceleratorServer = new AcceleratorServer(
				"1gFile.txt", 
				"C:\\Users\\brand\\Documents\\Computer Science\\CS 260\\DownloadAccelerator\\");
		acceleratorServer.createThreads();
		System.out.println("Threads created.");
		System.out.println("Dividing input file...");
		acceleratorServer.divideFile();
		System.out.println("File division completed.");
	}
}

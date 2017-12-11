import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.*;

public class AcceleratorServer {
	
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
			FileOutputStream file1 = new FileOutputStream(directory + "input55556.txt");
			FileOutputStream file2 = new FileOutputStream(directory + "input55557.txt"); 
			FileOutputStream file3 = new FileOutputStream(directory + "input55558.txt");
			FileOutputStream file4 = new FileOutputStream(directory + "input55559.txt");
			FileOutputStream file5 = new FileOutputStream(directory + "input55560.txt");
			FileInputStream inputFile = new FileInputStream(directory + fileName);
//			int fileSize = (int)inputFile.getChannel().size();
			byte[] buffer = new byte[1000];
			long partitionSize = (inputFile.getChannel().size())/5;
			
			System.out.println("Input file size: " + inputFile.getChannel().size() + ", Partition size : " + partitionSize);
			
			//write to first partition
			try {
				long remainingBytes = partitionSize;
				while (true) {	
					if (remainingBytes > 1000) {
						int numBytes = inputFile.read(buffer, 0, 1000);
						file1.write(buffer, 0, numBytes);
						remainingBytes = remainingBytes - numBytes;
					}
					else {
						int numBytes = inputFile.read(buffer, 0, (int)remainingBytes);
						file1.write(buffer, 0, numBytes);
						break;
					}
				}
			}
			catch (Exception e) {
				System.out.println("Error occurred in divideWrite function.");
			}
			
			//write to second partition
			try {
				long remainingBytes = partitionSize;
				while (true) {	
					if (remainingBytes > 1000) {
						inputFile.read(buffer, 0, 1000);
						file2.write(buffer);
						remainingBytes = remainingBytes - 1000;
					}
					else {
						inputFile.read(buffer, 0, (int)remainingBytes);
						file2.write(buffer);
						break;
					}
				}
			}
			catch (Exception e) {
				System.out.println("Error occurred in divideWrite function.");
			}
			
			//write to third partition
			try {
				long remainingBytes = partitionSize;
				while (true) {	
					if (remainingBytes > 1000) {
						inputFile.read(buffer, 0, 1000);
						file3.write(buffer);
						remainingBytes = remainingBytes - 1000;
					}
					else {
						inputFile.read(buffer, 0, (int)remainingBytes);
						file3.write(buffer);
						break;
					}
				}
			}
			catch (Exception e) {
				System.out.println("Error occurred in divideWrite function.");
			}
			
			//write to fourth partition
			try {
				long remainingBytes = partitionSize;
				while (true) {	
					if (remainingBytes > 1000) {
						inputFile.read(buffer, 0, 1000);
						file4.write(buffer);
						remainingBytes = remainingBytes - 1000;
					}
					else {
						inputFile.read(buffer, 0, (int)remainingBytes);
						file4.write(buffer);
						break;
					}
				}
			}
			catch (Exception e) {
				System.out.println("Error occurred in divideWrite function.");
			}
			
			//write to fifth partition
			try {
				long remainingBytes = inputFile.getChannel().size() - 4*partitionSize;
				while (true) {	
					if (remainingBytes > 1000) {
						inputFile.read(buffer, 0, 1000);
						file5.write(buffer);
						remainingBytes = remainingBytes - 1000;
					}
					else {
						inputFile.read(buffer, 0, (int)remainingBytes);
						file5.write(buffer);
						break;
					}
				}
			}
			catch (Exception e) {
				System.out.println("Error occurred in divideWrite function.");
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
	
	public class ServerSocketThread extends Thread {
		
		private DataOutputStream outputStream = null;
		private DataInputStream inputStream = null;
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
				//portNum = serverSocket.getLocalPort();
				System.out.println("ServerSocket objects created in Thread: " + portNum 
									+ ". Waiting for client to connect...");
				socket = serverSocket.accept();
				System.out.println("Connected to client in Thread: " + portNum);
				
				outputStream = new DataOutputStream(socket.getOutputStream());
				inputStream = new DataInputStream(socket.getInputStream());
				file = new File("C:\\Users\\brand\\Documents\\Computer Science\\CS 260\\DownloadAccelerator\\input" 
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
	    		outputStream.writeLong(partition.getChannel().size());
				System.out.println("Now sending partition to client in Thread: " + portNum);
		    	byte[] buffer = new byte[1000];
		    	while(true) {
			    	int numBytes = partition.read(buffer, 0, 1000);
			    	if(numBytes == -1) {
			    		break;
			    	}
			    	outputStream.write(buffer, 0, 1000);
			    	//System.out.println("Thread " + portNum + " sent " + numBytes + " bytes.");
			    	outputStream.flush();
			    	if(numBytes < 1000) {
			    		break;
			    	}
		    	}
		    	socket.close();
	    	}
	    	catch(Exception e) {
	    		System.out.println("Error occured while sending partition from " + this.getName());
	    		e.printStackTrace();
	    	}
	    }
	}
	
	public static void main(String[] args) {
		AcceleratorServer acceleratorServer = new AcceleratorServer(
				"100mFile.txt", 
				"C:\\Users\\brand\\Documents\\Computer Science\\CS 260\\DownloadAccelerator\\");
		acceleratorServer.createThreads();
		System.out.println("Dividing input file...");
		acceleratorServer.divideFile();
		System.out.println("File division completed.");
	}
}

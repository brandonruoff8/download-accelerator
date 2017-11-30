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
		fileDivider.divideFile(fileName, "D:\\Computer Science\\CS 260\\DownloadAccelerator");
	}
	
	public void createThreads() {
		serverSocketThread serverThread1 = new serverSocketThread(1515);
		serverSocketThread serverThread2 = new serverSocketThread(1616);
		serverSocketThread serverThread3 = new serverSocketThread(1717);
		serverSocketThread serverThread4 = new serverSocketThread(1818);
		serverSocketThread serverThread5 = new serverSocketThread(1919);		
	}
	
	public class serverSocketThread extends Thread {
		
		private ServerSocket serverSocket = null;
	    private Socket socket = null;
	    private File partition = null;
	    
	    public serverSocketThread(int portNum) {
			try {
				serverSocket = new ServerSocket(portNum);
				socket = serverSocket.accept();
				outputStream = socket.getOutputStream();
				partition = new File("D:\\Computer Science\\CS 260\\DownloadAccelerator\\" 
									+ portNum
									+ ".txt");
			}
			catch (Exception e) {
				System.out.println("Error creating ServerSocket in thread " + this.getName());
			}
	    }
	    
	    public void writeToClient() {
	    	
	    }
	}
	
	public static void main(String[] args) {
		AcceleratorServer acceleratorServer = new AcceleratorServer();
		acceleratorServer.divideFile();
		System.out.println("Dividing input file...");
		acceleratorServer.createThreads();
	}
	
	
	


}

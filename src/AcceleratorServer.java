import java.net.ServerSocket;
import java.net.Socket;

public class AcceleratorServer {
	
	FileDivider fileDivider = new FileDivider();
	
	public void divideFile() {
		fileDivider.divideFile("input.txt", "D:\\Computer Science\\CS 260\\DownloadAccelerator");
	}
	
	public void createThreads() {
		serverSocketThread thread1 = new serverSocketThread();
		serverSocketThread thread2 = new serverSocketThread();
		serverSocketThread thread3 = new serverSocketThread();
		serverSocketThread thread4 = new serverSocketThread();
		serverSocketThread thread5 = new serverSocketThread();		
	}
	
	public class serverSocketThread extends Thread {
		
		ServerSocket serverSocket = null;
		
		public void createSocket() {
			try {
				serverSocket = new ServerSocket(1515);
				serverSocket.accept();
			}
			catch (Exception e) {
				System.out.println("Error creating ServerSocket in thread " + this.getName());
			}

		}
	}
	


}

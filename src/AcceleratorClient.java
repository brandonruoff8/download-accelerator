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
		try {
			socketThread1.join();
			socketThread2.join();
			socketThread3.join();
			socketThread4.join();
			socketThread5.join();
		}
		catch (Exception e) {
			System.out.println("Error making main join() to Threads.");
		}

	}
	
	public void merge(String finalFileName) {
		String directory = "C:\\Users\\brand\\Documents\\Computer Science\\CS 260\\DownloadAccelerator\\";
		try {
			FileOutputStream finalFile = new FileOutputStream(directory + finalFileName);
			FileInputStream file1 = new FileInputStream(directory + "output55556.txt");
			FileInputStream file2 = new FileInputStream(directory + "output55557.txt");
			FileInputStream file3 = new FileInputStream(directory + "output55558.txt");
			FileInputStream file4 = new FileInputStream(directory + "output55559.txt");
			FileInputStream file5 = new FileInputStream(directory + "output55560.txt");
			
			byte[] buffer = new byte[(int)file1.getChannel().size()];
			file1.read(buffer);
			finalFile.write(buffer);			
			System.out.println("First file merged.");

			buffer = new byte[(int)file2.getChannel().size()];
			file2.read(buffer);
			finalFile.write(buffer);
			System.out.println("Second file merged.");
			
			buffer = new byte[(int)file3.getChannel().size()];
			file3.read(buffer);
			finalFile.write(buffer);
			System.out.println("Third file merged.");
			
			buffer = new byte[(int)file4.getChannel().size()];
			file4.read(buffer);
			finalFile.write(buffer);
			System.out.println("Fourth file merged.");
			
			buffer = new byte[(int)file5.getChannel().size()];
			file5.read(buffer);
			finalFile.write(buffer);
			System.out.println("Fifth file merged.");
			
			file1.close();
			file2.close();
			file3.close();
			file4.close();
			file5.close();
			finalFile.close();
		}
		catch (Exception e) {
			System.out.println("Error merging the five files to one.");
		}	
	}
	
	public class SocketThread extends Thread {
			
	    private DataInputStream inputStream = null;
	    private DataOutputStream outputStream = null;
	    
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
				outputStream = new DataOutputStream(socket.getOutputStream());
				outputFile = new FileOutputStream("C:\\Users\\brand\\Documents\\Computer Science\\CS 260\\DownloadAccelerator\\"
													+ "output" + portNum + ".txt");
				//create long variables to count the bytes read
			}
			catch (Exception e) {
				System.out.println("Error creating OutputStream in Thread :" + portNum);
			}
			
			try {
				long totalBytesRead = 0;
				long fileSize = inputStream.readLong();
				int numBytes;
				while(true) {
					byte[] buffer = new byte[1000];
				    //fill buffer with 10000 bytes from inputstream
					if(totalBytesRead + 1000 < fileSize) {
					    numBytes = inputStream.read(buffer, 0 , 1000);
					}
					else {
						numBytes = inputStream.read(buffer, 0, (int)(fileSize - totalBytesRead));
					}
				    //write the bytes in buffer to the output file
					outputFile.write(buffer, 0, numBytes);
					outputFile.flush();
					
					totalBytesRead = totalBytesRead + numBytes;
					//System.out.println("Thread " + portNum + " wrote " + numBytes + " bytes. Total bytes read:" + totalBytesRead);
					

					//if totalBytes is equal to the fileSize, then there is no more to be read
					if (totalBytesRead == fileSize) {
						break;
					}
				}
			}
			catch (Exception e) {
				System.out.println("Error receiving data from server in Thread: " + portNum);
				e.printStackTrace();
			}
			//}
			try {
	    		socket.close();
	    		outputFile.close();
	    		inputStream.close();
			}
			catch(Exception e) {
				System.out.println("Error closing socket, outputFile, and inputStream.");
			}
		}
	}
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		AcceleratorClient acceleratorClient = new AcceleratorClient();
		System.out.println("Reading from server...");
		acceleratorClient.createThreads();
		long end = System.currentTimeMillis();
		System.out.println("MultiThread time spent - NO MERGE: " + (end-start) + " milliseconds.");
		acceleratorClient.merge("originalFile.txt");
		end = System.currentTimeMillis();
		System.out.println("MultiThread time spent - WITH MERGE: " + (end-start) + " milliseconds.");
	}
}

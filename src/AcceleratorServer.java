import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.*;

public class AcceleratorServer {
	
	private FileDivider fileDivider = new FileDivider();
	private OutputStream outputStream = null;
    private InputStream inputStream = null;
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
				outputStream = socket.getOutputStream();
				file = new File("D:\\Computer Science\\CS 260\\DownloadAccelerator\\" 
						+ portNum
						+ ".txt");
				partition = new FileInputStream(file);
			}
			catch (Exception e) {
				System.out.println("Error creating ServerSocket in Thread: " + portNum);
			}
	    }
	    
	    public void writeToClient() {
	    	char symbol;
	    	try {
		    	for(int i = 0; i < file.length(); i++) 
		    	{
		    		symbol = (char)partition.read();
		    		outputStream.write(symbol);
		    	}
		    		
    		}
	    	catch (Exception e) {
	    		System.out.println("Error occured while sending partition.");
	    	}
	    }
	    
	    public void createReadThread() 
	    {
	        Thread readThread = new Thread() 
	        {
	            public void run() 
	            {
	                while (socket.isConnected()) 
	                {
	                    try 
	                    {
	                        byte[] readBuffer = new byte[200];
	                        int num = inputStream.read(readBuffer);

	                        if (num > 0) {
	                            byte[] arrayBytes = new byte[num];
	                            System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
	                            String recvedMessage = new String(arrayBytes, "UTF-8");
	                            System.out.println("Received message :" + recvedMessage);
	                        }/* else {
	                            // notify();
	                        }*/
	                        ;
	                        //System.arraycopy();
	                    }
	                    catch (SocketException se)
	                    {
	                        System.exit(0);
	                    }
	                    catch (IOException i) 
	                    {
	                        i.printStackTrace();
	                    }
	                }
	            }
	        };
	        readThread.setPriority(Thread.MAX_PRIORITY);
	        readThread.start();
	    }
	    
	    public void createWriteThread() 
	    {
	        Thread writeThread = new Thread() 
	        {
	            public void run() 
	            {
	                while (socket.isConnected()) 
	                {
	                    try 
	                    {
	                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	                        sleep(100);
	                        String typedMessage = inputReader.readLine();
	                        if (typedMessage != null && typedMessage.length() > 0) 
	                        {
	                            synchronized (socket) 
	                            {
	                                outputStream.write(typedMessage.getBytes("UTF-8"));
	                            }
	                        }/* else {
	                            notify();
	                        }*/
	                        ;
	                        //System.arraycopy();

	                    } 
	                    catch (IOException i) 
	                    {
	                        i.printStackTrace();
	                        break;
	                    } 
	                    catch (InterruptedException ie) 
	                    {
	                        ie.printStackTrace();
	                        break;
	                    }
	               }
	            }
	        };
	        writeThread.setPriority(Thread.MAX_PRIORITY);
	        writeThread.start();
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

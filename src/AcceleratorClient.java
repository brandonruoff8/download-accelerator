import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class AcceleratorClient {
	
    private InputStream inStream = null;
    private OutputStream outStream = null;
	
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
		                        int num = inStream.read(readBuffer);

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
		                                outStream.write(typedMessage.getBytes("UTF-8"));
		                                sleep(100);
		                            }
		                        }
		                        //System.arraycopy();
		                    } 
		                	catch (IOException i) 
		                	{
		                        i.printStackTrace();
		                    } 
		                	catch (InterruptedException ie) 
		                	{
		                        ie.printStackTrace();
		                    }
		                }
		            }
		        };
		        writeThread.setPriority(Thread.MAX_PRIORITY);
		        writeThread.start();
		    }
	}
	
	public static void main(String[] args) {
		AcceleratorClient acceleratorClient = new AcceleratorClient();
		acceleratorClient.createThreads();
	}
}

package EInvoice;
import EInvoice.RSACoder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;
public class SocketServer {
    private ServerSocket listener; // Listens for connection requests.
    private Socket connection;     // A socket for communicating with
	private final int ServerPort = 4564;
	public SocketServer() {
		try {
			init();
		}catch(IOException e){
			System.out.print("Socket Error");
		}
	}
    public static void main(String[] args) {
    	try {
        	SocketServer server = new SocketServer();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
	public void init() throws IOException {
		listener = new ServerSocket(ServerPort);
		System.out.println("Listening on port " + ServerPort);
		while (true) {
           System.out.println("wait");
           connection = listener.accept();
           System.out.println("Receive");
           new ConnectionHandler(connection,this);
        }
	}

    class ConnectionHandler extends Thread {
       // An object of this class is a thread that will
       // process the connection with one client.  The
       // thread starts itself in the constructor.

       Socket connection;    // A connection to the client.
       BufferedReader incoming;  // For reading data from the client.
       InputStream in;
       PrintWriter outgoing; // For transmitting data to the client.
       OutputStream out;
       SocketServer myServer;
       ConnectionHandler(Socket conn,SocketServer ms) {
             // Constructor.  Record the connection and
             // the directory and start the thread running.
          connection = conn;
          myServer = ms;
          start();
       }

       public void run() {
             // This is the method that is executed by the thread.
             // It creates streams for communicating with the client,
             // reads a command from the client, and carries out that
             // command.  The connection is logged to standard output.
             // An output beginning with ERROR indicates that a network
             // error occurred.  A line beginning with OK means that
             // there was no network error, but does not imply that the
             // command from the client was a legal command.
          String command = "Command not read";
          try {
             incoming = new BufferedReader( new InputStreamReader(connection.getInputStream()) );
             out = connection.getOutputStream();
             in = connection.getInputStream();
             
             System.out.println("try to receive message");
             while(true) {

                 String strFilePath;
				 FileOutputStream fos;
				 FileInputStream fis;
				 byte[] publicKey = new byte[128];
				 byte[] privateKey = new byte[512];
				 strFilePath = ".//publicKey";
			 	 try {
				 	 fis = new FileInputStream(strFilePath);
					 fis.read(publicKey);
					 fis.close();
				 } catch (Exception e) {
				
				 }
				 strFilePath = ".//privateKey";
				 privateKey = new byte[512]; 		
				 try {
					 fis = new FileInputStream(strFilePath);
					 fis.read(privateKey);
					 fis.close();
				 } catch (Exception e) {
				
				 }
				 
                 byte[] readBinData = new byte[64];
                 
                 in.read(readBinData,0,64);
                 System.out.println(new String(readBinData));
                 RSACoder.initKey();
                 byte[] decodedData = RSACoder.decryptByPrivateKey(readBinData, privateKey);
				 String result = new String(decodedData);
				 System.out.println("Decoded Data:\n" + result);
				 //String str = Base64.encodeBase64String(encodedData);
				 
             }
          }
          catch (Exception e) {
             System.out.println("ERROR " + connection.getInetAddress()
                                      + " " + command + " " + e);
          } finally {
             try {
                connection.close();
             }
             catch (IOException e) {
             }
          }
       }
    }
}

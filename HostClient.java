import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;
class HostClient {

    public static void main(String argv[]) throws Exception {
	String sentence;
	String userName;
	String hostName;
	String speed;
	String modifiedSentence = "";
	String initialMessage;
	boolean isOpen = true;
	int number = 1;
	boolean notEnd = true;
	String statusCode;
	boolean clientgo = true;
	int port = 12000;
	String serverName;
	int connectPort;
	Socket ControlSocket = null;
	
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("To connect to server, enter \"connect\" followed by the server's IP and port.");

	sentence = inFromUser.readLine();
	StringTokenizer tokens = new StringTokenizer(sentence);

	if(sentence.startsWith("connect ")) {
	    serverName = tokens.nextToken(); //passes connect command
	    serverName = tokens.nextToken();
	    connectPort = Integer.parseInt(tokens.nextToken());

	    try
		{
		    ControlSocket = new Socket(serverName, connectPort);

		    System.out.println("You are now connected to " + serverName);

		    do {

			DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream());

			DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));
			System.out.print("enter unsername: ");
			userName = inFromUser.readLine();
			System.out.print("enter hostname: ");
			hostName = inFromUser.readLine();
			System.out.print("enter speed: ");
			speed = inFromUser.readLine();
			initialMessage = userName + " " + hostName + " " + speed + '\n';
			outToServer.writeBytes(initialMessage);

				
			File sharedF = new File("sharedFiles.txt");
			FileInputStream fileContents = new FileInputStream(sharedF);
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileContents));
			port = port + 2;
 			outToServer.writeBytes(port + "" + '\n');
			ServerSocket sendData = new ServerSocket(port);
			Socket dataSocket = sendData.accept();
			System.out.println("Data Socket opened.");
			DataOutputStream dataOutToServer = new DataOutputStream(dataSocket.getOutputStream());
			String fileLine;
			while ((fileLine = fileReader.readLine()) != null) {
					fileLine = (fileLine + "\n");
					dataOutToServer.writeBytes(fileLine);
					}

			dataSocket.close();
			fileContents.close();
			sendData.close();
			
			
			sentence = "quit";
			
			
			
			
			
			if (sentence.equals("list")) {



			} 
				
				else if (sentence.startsWith("retr ")) {


	        	} 

			else if (sentence.equals("quit")) {
                    System.out.print("qutting");
                    outToServer.writeBytes(port + " " + sentence + " " + '\n');
                    isOpen = false;
				}
		    } while (isOpen);
		}
	    catch (IOException ioEx)
		{
		    ioEx.printStackTrace();
		}
	    finally
		{
		    try
			{
			    System.out.println("\nClosing connection...");
			    ControlSocket.close();
			}
		    catch (IOException ioEx)
			{
			    System.out.println("Unable to disconnect.");
			    System.exit(1);
			}
		}

	} else {
	    System.out.println("Must enter \"connect\" to connect to server.");
	    System.exit(1);
	}
    }
    
    private static String getFiles() {
        String files = "";
        File cwd = new File(".");

        File[] filesList = cwd.listFiles();
        if (filesList.length == 0) {
            return "No files available.";
        } else {
            for (File f : filesList) {
                if (f.isFile()) {
                    files = files + f.getName() + " ";
                }
            }
            return files;
        }
    }
    
}

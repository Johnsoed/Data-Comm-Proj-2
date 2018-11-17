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
			
			String listFiles = getFiles();
            listFiles = listFiles + '\n';
            outToServer.writeBytes(listFiles);
            
			
			
			sentence = "quit";
			
			
			
			
			
			if (sentence.equals("list")) {
			    port = port + 2;
			    System.out.println(port);
			    outToServer.writeBytes(port + " " + sentence + " " + '\n');
			    ServerSocket welcomeData = new ServerSocket(port);
			    Socket dataSocket = welcomeData.accept();

			    DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));

			    System.out.println("There is data to read");
			    modifiedSentence = inData.readLine();
			    System.out.println("Listing Files: ");
			    System.out.println(modifiedSentence);

			    welcomeData.close();
			    dataSocket.close();


			} else if (sentence.startsWith("retr ")) {
			    StringTokenizer tokens2 = new StringTokenizer(sentence);
			    tokens2.nextToken();
			    String filename = tokens2.nextToken();
			    port = port + 2;
			    System.out.println(port);
			    outToServer.writeBytes(port + " " + sentence + " " + '\n');
			    ServerSocket welcomeData = new ServerSocket(port);
				FileWriter fw = new FileWriter(filename);
			    Socket dataSocket = welcomeData.accept();
			    DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
				do {
				    modifiedSentence = inData.readLine();
					if (modifiedSentence != null)
					{
				    modifiedSentence = (modifiedSentence + "\n");
					fw.write(modifiedSentence);
					}
				} while(modifiedSentence != null);
				  fw.flush();
				  dataSocket.close();
			      welcomeData.close();

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

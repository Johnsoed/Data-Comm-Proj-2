package mainServer;

import java.io.*; 
import java.net.*;
import java.util.*;
import mainServer.User;
import mainServer.SharedFile;

class CentralServer {

    public static void main(String args[]) throws Exception{

	ServerSocket welcomeSocket;
	int port = 12000;


	try
	    {
		welcomeSocket  = new ServerSocket(port);
		while (true) {
		    Socket connectionSocket = welcomeSocket.accept();

		    System.out.println("\nNew client connected.");

		    ClientHandler handler = new ClientHandler(connectionSocket);

		    handler.start();
		}
	    }
	catch (IOException ioEX)
	    {
		System.out.println("\nError in setting up port.");
		System.exit(1);
	    }

    }
}

class ClientHandler extends Thread
{
    private DataOutputStream outToClient;
    private BufferedReader inFromClient;
    private String fromClient;
    private String clientCommand;
    private String clientFileName = "";
    private String initialMessage;
    private String userName;
    private String hostName;
	private String ourFileName;
	private String description;
	private String fileHostname;
    private String speed;
    private Boolean inTable = false;
	private Boolean exists;
    String fileLine;
    private byte[] data;
    private String frstln;
    private Boolean closed = false;
    private Socket connectionSocket;
    private int port;
    ArrayList<User> userList = new ArrayList<User>();
    ArrayList<String> hostList = new ArrayList<String>();
    ArrayList<SharedFile> sharedFileList = new ArrayList<SharedFile>();
	
    public ClientHandler(Socket socket) {

	try
	    {
		connectionSocket = socket;
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		port = connectionSocket.getPort();
	    }
	catch (IOException ioEx)
	    {
		ioEx.printStackTrace();
		}
	}
		
    public void run() {
	try
	    {

			initialMessage = inFromClient.readLine();

            File userTable = new File("userTable.txt");
            exists = userTable.exists();
			if (exists == true){
			FileInputStream tableContents = new FileInputStream(userTable);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(tableContents));
            while ((fileLine = fileReader.readLine()) != null) {
                StringTokenizer tableTokens = new StringTokenizer(fileLine);
                userName = tableTokens.nextToken();
                hostName = tableTokens.nextToken();
                speed = tableTokens.nextToken();
                userList.add( new User(userName,hostName,speed) );
                hostList.add(hostName);
			}
			tableContents.close();
			}
			
			
            File fileTable = new File("fileTable.txt");
            exists = fileTable.exists();
			if (exists == true){
			FileInputStream tableContents = new FileInputStream(fileTable);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(tableContents));
            while ((fileLine = fileReader.readLine()) != null) {
				String [] fileInfo = fileLine.split(":");
				ourFileName = fileInfo[0];
				description = fileInfo[1];
				fileHostname = fileInfo[2];
				sharedFileList.add(new SharedFile(ourFileName,description,fileHostname));

				
			}
			tableContents.close();
			}			
			
			
			
			
			
			
			
			
			
			
			StringTokenizer tokens = new StringTokenizer(initialMessage);
			userName = tokens.nextToken();
			hostName = tokens.nextToken();
			speed = tokens.nextToken();
			
            if(hostList.contains(hostName)){
                inTable = true;
            }
 
            System.out.println(inTable);
            

			
			
			if(inTable == false){
                userList.add( new User(userName,hostName,speed) );
			}
			
			for (int i = 0; i < userList.size(); i++){
                System.out.println(userList.get(i));
			}
        
            String tempString;
            FileWriter userTableWriter = new FileWriter("userTable.txt");
            for (int i = 0; i < userList.size(); i++){
                tempString = userList.get(i) + "" + '\n';
                userTableWriter.write(tempString);
            }
            userTableWriter.flush();
        
			String modifiedSentence;
			String portMessage = inFromClient.readLine();
			port = Integer.parseInt(portMessage);
			Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
			DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));;
			do {
				modifiedSentence = inData.readLine();
				if (modifiedSentence != null){
					System.out.println(modifiedSentence);
				    modifiedSentence = (modifiedSentence + "\n");
					}
				} while(modifiedSentence != null);
			dataSocket.close();

		
		
		
		do
		    {        
			fromClient = inFromClient.readLine();
			System.out.println(fromClient);
            StringTokenizer commandTokens = new StringTokenizer(fromClient);
			frstln = commandTokens.nextToken();
			port = Integer.parseInt(frstln);
			clientCommand = commandTokens.nextToken();


            
			
			
			
			
			if (clientCommand.equals("list")) {

			}
			

			else if (clientCommand.equals("retr")) {

			} 
			

		
		    } while (!clientCommand.equals("quit"));
	    }
	catch(IOException ioEx)
	    {
		ioEx.printStackTrace();
	    }
	try
	    {
		if (connectionSocket != null)
		    {
			String inetadd = "" + connectionSocket.getInetAddress();
			System.out.println("\n" + inetadd.substring(1) + " has disconnected");
			connectionSocket.close();
		    }
	    }
	catch (IOException ioEX)
	    {
		System.out.println("Unable to disconnect.");
	    }
    }

    private String getFiles() {
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

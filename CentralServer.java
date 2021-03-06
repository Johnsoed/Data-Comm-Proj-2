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
	private String fileHostName;
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
    ArrayList<String> searchResults = new ArrayList<String>();
	
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
                StringTokenizer fileTokens = new StringTokenizer(fileLine,":");
				ourFileName = fileTokens.nextToken();
				description = fileTokens.nextToken();
				fileHostName = fileTokens.nextToken();
				sharedFileList.add(new SharedFile(ourFileName,description,fileHostName));
				
				
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
			DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
			String[] parts;
			do {
				modifiedSentence = inData.readLine();
				if (modifiedSentence != null){
					System.out.println(modifiedSentence);
					modifiedSentence = modifiedSentence + ":" + hostName;
					parts = modifiedSentence.split(":");		
					ourFileName = parts[0];
					description = parts[1];
					fileHostName = hostName;
					if (inTable == false){
                        sharedFileList.add( new SharedFile(ourFileName,description,fileHostName) );
                        }
					}
				} while(modifiedSentence != null);
			dataSocket.close();

            FileWriter fileTableWriter = new FileWriter("fileTable.txt");
            for (int i = 0; i < sharedFileList.size(); i++){
                tempString = sharedFileList.get(i) + "" + '\n';
                fileTableWriter.write(tempString);
            }
            fileTableWriter.flush();
		
		
		
		do
		    {        
			fromClient = inFromClient.readLine();
			System.out.println(fromClient);
            StringTokenizer commandTokens = new StringTokenizer(fromClient);
			frstln = commandTokens.nextToken();
			port = Integer.parseInt(frstln);
			clientCommand = commandTokens.nextToken();
			System.out.println(clientCommand);


            
			
			
			
			
			if (clientCommand.equals("search")) {
                System.out.println("hi");
                String keyword = "files";
                keyword = commandTokens.nextToken();
                String tempSpeed = "none";
                String tempDescription;
                for (int i = 0; i < sharedFileList.size(); i++){
                    tempDescription = sharedFileList.get(i).description;
                    if(tempDescription.contains(keyword)){
                    for (int j = 0; j < userList.size(); j++){
                        if(userList.get(j).hostName.equals(sharedFileList.get(i).hostName)){
                            tempSpeed = userList.get(j).connectionSpeed;
                        }
                    }
                    System.out.println(sharedFileList.get(i).fileName + " " + sharedFileList.get(i).hostName + 
                    " " + tempSpeed);
                    searchResults.add(sharedFileList.get(i).fileName + " " + sharedFileList.get(i).hostName + 
                    " " + tempSpeed);
                    }
                }
                
                Socket searchSocket = new Socket(connectionSocket.getInetAddress(), port);
                DataOutputStream searchData = new DataOutputStream(searchSocket.getOutputStream());
                
                
                for (int k = 0; k < searchResults.size(); k++){
                tempString = searchResults.get(k) + "" + '\n';
                System.out.println(tempString);
                searchData.writeBytes(tempString);
                    }
                searchSocket.close();
                searchResults.clear();
                
            
			}
			

			else if (clientCommand.equals("retr")) {

			} 
			

		
		    } while (!clientCommand.equals("quit"));
	    

		if (connectionSocket != null)
		    {
			String inetadd = "" + connectionSocket.getInetAddress();
			System.out.println("\n" + inetadd.substring(1) + " has disconnected");
			connectionSocket.close();
		    }
        }
	catch (Exception e)
	    {
            e.printStackTrace();
			String inetadd = "" + connectionSocket.getInetAddress();
			System.out.println("\n" + inetadd.substring(1) + " has disconnected");
			try {
			connectionSocket.close();
			}
			catch (IOException disEX){
			System.out.println("unable to disconnect");
			}
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

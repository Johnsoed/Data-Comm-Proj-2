package mainServer;

import java.io.*; 
import java.net.*;
import java.util.*;
import mainServer.User;

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
    private String speed;
    String fileLine;
    private byte[] data;
    private String frstln;
    private Boolean closed = false;
    private Socket connectionSocket;
    private int port;
    ArrayList<User> userList = new ArrayList<User>();
	
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
		do
		    {
			initialMessage = inFromClient.readLine();

            File userTable = new File("userTable.txt");
            boolean exists = userTable.exists();
			if (exists == true){
			FileInputStream tableContents = new FileInputStream(userTable);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(tableContents));
            while ((fileLine = fileReader.readLine()) != null) {
                StringTokenizer tableTokens = new StringTokenizer(fileLine);
                userName = tableTokens.nextToken();
                hostName = tableTokens.nextToken();
                speed = tableTokens.nextToken();
                userList.add( new User(userName,hostName,speed) );
			}
			tableContents.close();
			}
			
			StringTokenizer tokens = new StringTokenizer(initialMessage);
			userName = tokens.nextToken();
			hostName = tokens.nextToken();
			speed = tokens.nextToken();
			userList.add( new User(userName,hostName,speed) );
			
			
			for (int i = 0; i < userList.size(); i++){
                System.out.println(userList.get(i));
			}
            System.out.println(userList.size());
            String fileString = inFromClient.readLine();
            System.out.println(fileString);
        
            String tempString;
            FileWriter userTableWriter = new FileWriter("userTable.txt");
            for (int i = 0; i < userList.size(); i++){
                tempString = userList.get(i) + "" + '\n';
                userTableWriter.write(tempString);
            }
            userTableWriter.flush();
        
        
        
			fromClient = inFromClient.readLine();
			System.out.println(fromClient);
            StringTokenizer commandTokens = new StringTokenizer(fromClient);
			frstln = commandTokens.nextToken();
			port = Integer.parseInt(frstln);
			clientCommand = commandTokens.nextToken();


            if (clientCommand.equals("list")) {
                System.out.println("Listing files...");
                System.out.println(port);
                Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);


                System.out.println("Data Socket opened.");

                DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());

                String fileList = getFiles();

                //Testing
                System.out.println(fileList);

                dataOutToClient.writeBytes(fileList);
                dataSocket.close();
				System.out.println("Data Socket closed");
			}
			

			else if (clientCommand.equals("retr")) {
			    try {
				String fileLine;
				clientFileName = tokens.nextToken();
				System.out.println(clientFileName);
				Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
				System.out.println("Data Socket opened.");
				File f = new File(clientFileName);

				FileInputStream fileContents = new FileInputStream(f);
				

				DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
				System.out.println("retr socket open");
				BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileContents));
				while ((fileLine = fileReader.readLine()) != null) {
						fileLine = (fileLine + "\n");
						dataOutToClient.writeBytes(fileLine);
							}
				System.out.println(clientFileName);

				dataSocket.close();
				fileContents.close();
			    }
			    catch (Exception e) {
				System.out.println("Error opening file.");
			    }
			} 
			
			else if (clientCommand.equals("stor")){

				String fileLine;
				String modifiedSentence;
				clientFileName = tokens.nextToken();
				Socket dataSocket = new Socket(connectionSocket.getInetAddress(), port);
				System.out.println(clientFileName);
				System.out.println(port);
				FileWriter fw = new FileWriter(clientFileName);
				
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

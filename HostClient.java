package mainClient;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;
public class HostClient {

    private Socket ControlSocket;
    private int port;
    private int portFTP;
    private DataOutputStream outToServer;
	private Socket ftpSocket;
    private DataOutputStream ftpOut;
    private DataInputStream ftpIn;

    public void createa(String ips, String ports, String users, String hosts, String speeds) {
        String sentence;
        String userName;
        String hostName;
        String speed;
        String initialMessage;
        boolean isOpen = true;
        int number = 1;
        boolean notEnd = true;
        String statusCode;
        boolean clientgo = true;
        port = 12000;
        String serverName;
        int connectPort;
        ControlSocket = null;
		ftpSocket = null;
		ftpOut = null;
		ftpIn = null;



        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));






            serverName = ips;
            connectPort = Integer.parseInt(ports);

            try {
                ControlSocket = new Socket(serverName, connectPort);

                System.out.println("You are now connected to " + serverName);



                    outToServer = new DataOutputStream(ControlSocket.getOutputStream());

                    DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));
                    userName = users;
                    hostName = hosts;
                    speed = speeds;
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




            } catch (IOException ioEx) {
                ioEx.printStackTrace();
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

    public void closeserver(){
        try {
            ControlSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String commands(String sentence) {
       if (sentence.startsWith("connect")) {
			String serverNameFTP;
			StringTokenizer FTPTokens = new StringTokenizer(sentence);
	    	serverNameFTP = FTPTokens.nextToken(); //passes connect command
	    	serverNameFTP = FTPTokens.nextToken();
			portFTP = Integer.parseInt(FTPTokens.nextToken());

		   
		  	try {
		   		ftpSocket = new Socket(serverNameFTP, portFTP);
                ftpOut = new DataOutputStream(ftpSocket.getOutputStream());
                ftpIn = new DataInputStream(new BufferedInputStream(ftpSocket.getInputStream()));
			}
		   	catch (IOException ioEx)
			{
		    ioEx.printStackTrace();
			}
		   
           return ("You are now connected to " + serverNameFTP);
        }

        else if (sentence.startsWith("retr ")){
            try{
                ftpIn = new DataInputStream(new BufferedInputStream(ftpSocket.getInputStream()));
                String modifiedSentence = "";
                StringTokenizer tokens2 = new StringTokenizer(sentence);
			    tokens2.nextToken();
			    String filename = tokens2.nextToken();
			    portFTP = portFTP + 2;
			    System.out.println(portFTP);
			    ftpOut.writeBytes(portFTP + " " + sentence + '\n');
			    ServerSocket welcomeData = new ServerSocket(portFTP);
				FileWriter fw = new FileWriter(filename);
			    Socket retrSocket = welcomeData.accept();
                DataInputStream retrData = new DataInputStream(new BufferedInputStream(retrSocket.getInputStream()));
				do {
				    modifiedSentence = retrData.readLine();
					if (modifiedSentence != null)
					{
				    modifiedSentence = (modifiedSentence + '\n');
					fw.write(modifiedSentence);
					}
				} while(modifiedSentence != null);
                    fw.flush();
                    retrSocket.close();
                    welcomeData.close();
                    ftpOut.flush();
                    }
                    catch(Exception e){
                        System.out.println("something went wrong");
                    }
        
        

            return "Successfully downloaded " + sentence.subSequence(sentence.lastIndexOf(" "),sentence.length() );
        }

        else if (sentence.equals("quit")) {
        
        try{
                System.out.print("qutting");
                System.out.println(portFTP + " " + sentence + '\n');
                ftpOut.writeBytes(portFTP + " " + sentence + '\n');
                ftpSocket.close();
            }
            catch(Exception quiteE){
            System.out.println("something when wrong quitting");
            }
                


           return "Disconnected from server";
        }
        else
        {
            return "invalid command";
        }

    }
    public ArrayList<String> Search(String key) {
        ArrayList<String> searchArray = new ArrayList<String>();
        try {
            port = port + 2;
            searchArray.clear();
            outToServer.writeBytes(port + " search " + key + " " + '\n');
            ServerSocket welcomeSearch = new ServerSocket(port);
            Socket searchResultSocket = welcomeSearch.accept();
            DataInputStream inResults = new DataInputStream(new BufferedInputStream(searchResultSocket.getInputStream()));
            String searchString;
            do {
                searchString = inResults.readLine();
                if (searchString != null) {
                    searchArray.add(searchString);

                }
            } while (searchString != null);
            welcomeSearch.close();


        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }

        return searchArray;
    }


}

package edu.gvsu.cs351.conversion;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;
public class HostClient {

    private Socket ControlSocket;
    private int port;
    private DataOutputStream outToServer;


    public void createa(String ips, String ports, String users, String hosts, String speeds) {
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
        port = 12000;
        String serverName;
        int connectPort;
        ControlSocket = null;

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


           return "Connected to "+ sentence.subSequence(sentence.lastIndexOf(" "),sentence.length() -1);
        }

        else if (sentence.startsWith("retr ")){

            return "Successfully downloaded " + sentence.subSequence(sentence.lastIndexOf(" "),sentence.length() );
        }

        else if (sentence.equals("quit")) {


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

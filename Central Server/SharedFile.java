package mainServer;

public class SharedFile {
String fileName;
String description;
String hostName;

    public SharedFile(String fileName, String description, String hostName){
        this.fileName = fileName;
        this.description = description;
        this.hostName = hostName;
    }
    @Override
    public String toString() {
    return fileName + ":" + description + ":" + hostName;
    }
}

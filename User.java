package mainServer;

public class User {
String userName;
String hostName;
String connectionSpeed;

    public User(String userName, String hostName, String connectionSpeed){
        this.userName = userName;
        this.hostName = hostName;
        this.connectionSpeed = connectionSpeed;
    }
    @Override
    public String toString() {
    return userName + " " + hostName + " " + connectionSpeed;
    }
}

package Client;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private String username = "Braden";

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        System.out.println("Welcome to the Chat Client");
        System.out.print("Please Input a Username to Begin: ");
        try (Scanner usernameScanner = new Scanner(System.in)) {
            username = usernameScanner.nextLine();
        }
        connect("localhost", 1234, username);
    }

    public void connect(String ipAddress, int port, String username) {
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException i) {
            System.out.println("Error Occured When Trying to Connecting to Chat Server");
            System.out.println(i);
        }
    }

}
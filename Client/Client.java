package Client;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public boolean isConnected;

    private Socket socket;
    private String username;

    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;

    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

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
        initStreams();

        //System.out.println("Listening for Packets");
        //listenForPackets();
    }

    public void connect(String ipAddress, int port, String username) {
        try {
            socket = new Socket(ipAddress, port);
            isConnected = true;
            System.out.println("Connected to Server!");
        } catch (IOException i) {
            System.out.println("Error Occured When Trying to Connecting to Chat Server");
            System.out.println(i);
        }
    }

    private void initStreams() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException i) {
            System.out.println("Error Occured While Starting I/O Streams");
            System.out.println(i);
        }

        System.out.println("test");
    }

    public void listenForPackets() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isConnected) {
                    Packet packet = null;

                    try {
                        packet = (Packet) objectInputStream.readObject();
                    } catch (IOException i) {
                        System.out.println("Error Occured While Trying to Get Packet From Client");
                        System.out.println(i);
                    } catch (ClassNotFoundException c) {
                        System.out.println("Error Occured: Packet Class Not Found!");
                        System.out.println(c);
                    }
                }
            }
        }).start();
    }

    public void sendInit() {
        Packet initPacket = new Packet();
        initPacket.type = PacketType.INIT;
        initPacket.sender = username;

        sendPacket(initPacket);
    }

    public void sendPacket(Packet packet) {
        try {
            objectOutputStream.writeObject(packet);
        } catch (IOException i) {
            System.out.println("Error Occured While Trying to Send Packet");
            System.out.println(i);
        }
    }

}
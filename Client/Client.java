package Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

import Packet.Packet;
import Packet.PacketType;

public class Client {

    private Socket socket;
    private String username;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private Scanner messageScanner;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        System.out.println("Welcome to the Chat Client");
        System.out.print("Please Input a Username to Begin: ");

        Scanner usernameScanner = new Scanner(System.in);
        username = usernameScanner.nextLine();

        connect("localhost", 1234);

        initStreams();

        listenForPackets();

        sendInit();

        clientLoop();

    }

    private void connect(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
            System.out.println("Connected to Server!");
        } catch (IOException i) {
            System.out.println("Error Occured When Trying to Connecting to Chat Server");
            System.out.println(i);
        }
    }

    private void initStreams() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException i) {
            System.out.println("Error Occured While Starting I/O Streams");
            System.out.println(i);
        }
    }

    private void listenForPackets() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (socket.isConnected()) {
                    Packet packet = null;

                    try {
                        packet = (Packet) objectInputStream.readObject();
                    } catch (IOException i) {
                        System.out.println("Server Has Closed!");
                        System.exit(0);
                    } catch (ClassNotFoundException c) {
                        System.out.println("Error Occured: Packet Class Not Found!");
                        System.out.println(c);
                    }

                    if (packet != null) {
                        switch (packet.type) {
                            case MESSAGE:
                                System.out.println(packet.data);
                                break;
                        }
                    }

                }
            }
        }).start();
    }

    public void clientLoop() {
        while (socket.isConnected()) {
            getMessage();
        }
    }

    public void getMessage() {
        String message = null;
        Scanner messageScanner = new Scanner(System.in);
        message = messageScanner.nextLine();

        sendMessage(message);
    }

    public void sendMessage(String message) {
        Packet messagePacket = new Packet();
        messagePacket.type = PacketType.MESSAGE;
        messagePacket.sender = username;
        messagePacket.data = message;

        sendPacket(messagePacket);
    }

    public void sendInit() {
        Packet initPacket = new Packet();
        initPacket.type = PacketType.INIT;
        initPacket.sender = username;

        sendPacket(initPacket);
    }

    private void sendPacket(Packet packet) {
        try {
            objectOutputStream.writeObject(packet);
        } catch (IOException i) {
            System.out.println("Error Occured While Trying to Send Packet");
            System.out.println(i);
        }
    }

}
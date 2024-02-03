package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Packet.Packet;
import Packet.PacketType;

public class ClientHandler {

    public boolean isInitalized;

    private boolean isListening;
    
    public Socket socket;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public String username;

    public String message;

    public ClientHandler(ServerSocket serverSocket) {
        acceptClient(serverSocket);
        initStreams();

        isListening = true;
        listenForPackets();
    }

    public void acceptClient(ServerSocket serverSocket) {
        try {
            socket = serverSocket.accept();
        } catch (IOException i) {
            System.out.println("Error Occured While Listening for Client");
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

                while (socket.isConnected() && isListening) {
                    Packet packet = null;

                    try {
                        packet = (Packet) objectInputStream.readObject();
                    } catch (IOException i) {
                        try {
                            socket.close();
                        } catch (IOException o) {
                            System.out.println("Error Occured Trying to Close Client Socket!");
                            System.out.println(o);
                        }
                    } catch (ClassNotFoundException c) {
                        System.out.println("Error Occured: Packet Class Not Found!");
                        System.out.println(c);
                    }

                    if (packet != null) {
                        switch (packet.type) {
                            case INIT:
                                username = packet.sender;
                                isInitalized = true;
                                break;
                            case MESSAGE:
                                message = username + ": " + packet.data;
                                break;
                        }
                    }

                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        Packet messagePacket = new Packet();
        messagePacket.type = PacketType.MESSAGE;
        messagePacket.sender = "SERVER";
        messagePacket.data = message;

        sendPacket(messagePacket);
    }

    private void sendPacket(Packet packet) {
        try {
            objectOutputStream.writeObject(packet);
        } catch (IOException i) {
            System.out.println("Error Occured While Trying to Send Packet");
            System.out.println(i);
        }
    }

    public void exit() {
        isListening = false;
    }
}

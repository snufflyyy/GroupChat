package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler {
    
    public Socket socket;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public String username;

    public ClientHandler(ServerSocket serverSocket) {
        acceptClient(serverSocket);

        System.out.println("Listening for Packets");
        listenForPackets();
    }

    public void acceptClient(ServerSocket serverSocket) {
        try {
            socket = serverSocket.accept();
            System.out.println("Connected to Client");

            try {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException i) {
                System.out.println("Error Occured While Starting I/O Streams");
                System.out.println(i);
            }

        } catch (IOException i) {
            System.out.println("Error Occured While Listening for Client");
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
                        System.out.println("Error Occured While Trying to Get Packet From Client");
                        System.out.println(i);
                    } catch (ClassNotFoundException c) {
                        System.out.println("Error Occured: Packet Class Not Found!");
                        System.out.println(c);
                    }

                    if (packet != null) {
                        switch (packet.type) {
                            case INIT:
                                packet.sender = username;
                                System.out.println(username);
                                break;
                        }
                    }

                }
            }
        }).start();
    }
}

package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clientHandlers;

    private int connectedClients;

    public static void main(String[] args) {
        Server chatServer = new Server();
        chatServer.startServer(1234);
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started Server!");

            System.out.println("Listening at Port: " + port);
            listenForClients();

        } catch (IOException i) {
            System.out.println("Failed to Start Server Socket!");
            System.out.println(i);
        }
    }

    public void listenForClients() {
        new Thread(new Runnable() {
            @Override
            public void run() { 
                if (connectedClients == 0 || clientHandlers.size() - 1 != connectedClients + 1) {
                    clientHandlers = new ArrayList<>();
                    clientHandlers.add(new ClientHandler(serverSocket));
                }

                if (clientHandlers.get(connectedClients).socket.isConnected()) {
                    System.out.println(clientHandlers.get(connectedClients).username + " Has Connected!");
                    connectedClients++;
                }

            }
        }).start();   
    }

}

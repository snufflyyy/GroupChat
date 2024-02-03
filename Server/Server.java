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

            clientHandlers = new ArrayList<>();

            System.out.println("Listening at Port: " + port);
            listenForClients();

        } catch (IOException i) {
            System.out.println("Failed to Start Server Socket!");
            System.out.println(i);
        }

        while (!(connectedClients >= 1)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException i) {

            }
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException i) {
            
        }

        broadcastMessage("test");

        //disconnectChecker();
    }

    public void listenForClients() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (connectedClients == clientHandlers.size()) {
                    clientHandlers.add(new ClientHandler(serverSocket));

                    while (!clientHandlers.get(connectedClients).isInitalized) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.out.println("Error Occured While Checking if Client is Initalized!");
                            System.exit(1);
                        }
                    }

                    if (clientHandlers.get(connectedClients).socket.isConnected()) {
                        System.out.println(clientHandlers.get(connectedClients).username + " Has Connected!");
                        connectedClients++;
                    }
                }
            }
        }).start();   
    }

    public void broadcastMessage(String message) {
        for (int i = 0; i < clientHandlers.size(); i++) {
            System.out.println("Sent Message!");
            clientHandlers.get(i).sendMessage(message);
        }
    }
}

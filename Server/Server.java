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
            clientHandlers.add(new ClientHandler());

            listenForClients();
            System.out.println("Listening at Port: " + port);

        } catch (IOException i) {
            System.out.println("Failed to Start Server Socket!");
            System.out.println(i);
        }
    }

    public void listenForClients() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (clientHandlers.size() != connectedClients + 1) {
                        clientHandlers.add(new ClientHandler(ser));
                    }
                    try {
                        if (!clientHandlers.get(connectedClients).isConnected) {
                            clientHandlers.get(connectedClients).socket = serverSocket.accept();
                            clientHandlers.get(connectedClients).isConnected = true;
                            clientHandlers.get(connectedClients).listenForPackets();
                            connectedClients++;

                            System.out.println(clientHandlers.get(connectedClients - 1).username + " has Joined!");
                        }
                    } catch (IOException i) {
                        System.out.println("Error occured while listening for clients");
                        System.out.println(i);
                    }
                }
            }
        }).start();
    }


}

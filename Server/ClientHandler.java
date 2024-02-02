package Server;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler {

    public boolean isConnected = false;
    
    public Socket socket;

    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

    public String username;

    public ClientHandler(ServerSocket serverSocket) {
        try {
            socket = serverSocket.accept();
        } catch (IOException i) {
            System.out.println("Error Occured While Trying to Connect to Client");
        }

        initStreams();
        listenForPackets();
    }

    public void initStreams() {
        try {
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException i) {
            System.out.println("Error Occured While Initializing Input Streams");
            System.out.println(i);
        }
    }

    public void listenForPackets() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    Packet packet = null;

                    try {
                        packet = (Packet) objectInputStream.readObject();
                        
                        switch (packet.type) {
                            case INIT:
                                username = packet.data;
                                break;
                            case MESSAGE:
                                // do stuff
                        }
                    } catch (IOException i) {
                        System.out.println("Error Occured While Reading Packet");
                    } catch (ClassNotFoundException c) {
                        System.out.println("Error Occured: Packet Class Missing");
                    }
                }
            }
        }).start();
    }

}

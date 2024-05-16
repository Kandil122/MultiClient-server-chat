package Server1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static List<Socket> sockets = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1236)) {
            System.out.println("Server started. Listening on port 1236...");

            while (true) {
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                System.out.println("Client connected.");

                new Thread(() -> {
                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
                            String message = new String(buffer, 0, bytesRead);
                            broadcast(message, socket);
                        }
                    } catch (IOException e) {
                        System.out.println("A client has disconnected.");
                    } finally {
                        sockets.remove(socket);
                        System.out.println("A client has left the chat.");
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String message, Socket socketSent) {
        if (message.trim().equals("quit")) {
            sockets.remove(socketSent);
        } else {
            for (Socket socket : sockets) {
                if (socket != socketSent) {
                    try {
                        socket.getOutputStream().write(message.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


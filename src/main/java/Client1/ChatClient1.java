package Client1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a username to join the chat: ");
        String username = scanner.nextLine();

        try (Socket socket = new Socket("localhost", 1236);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(username + " has joined the chat.");

            Thread readThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println("\u001B[33m" + message + "\u001B[0m");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readThread.start();

            String input;
            while (!(input = scanner.nextLine()).equals("quit")) {
                writer.println(username + ": " + input);
            }

            writer.println(username + " has left the chat.");
        } catch (IOException e) {
            System.out.println("The server seems to have been shut down...");
        }
    }
}

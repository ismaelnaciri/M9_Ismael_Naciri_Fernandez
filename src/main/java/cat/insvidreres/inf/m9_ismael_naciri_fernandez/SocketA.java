package cat.insvidreres.inf.m9_ismael_naciri_fernandez;


import cat.insvidreres.inf.m9_ismael_naciri_fernandez.utils.IsmaUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SocketA {

    private static Socket clientSocket;
    private int serverPort;


    public SocketA(int serverPort) {
        this.serverPort = serverPort;

        try {
            clientSocket = new Socket("localhost", serverPort);

        } catch (ConnectException e) {
            System.out.println("Connection refused | " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error  |  " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        int serverPort = 7777;
        SocketA socket = null;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Desired algorithm?");
        String text = scanner.nextLine();

        String hashAsString = hashClientString(text);

        try {
            socket = new SocketA(serverPort);
            socket.sendMessage(hashAsString);
        } finally {
            if (socket != null) {
                closeSockets();
            }
        }
    }

    private void sendMessage(String hashAsString) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(hashAsString);
            System.out.println("Message sent successfully");
        } catch (Exception e) {
            System.out.println("Error  |  " + e.getMessage());
        }
    }

    public static String hashClientString(String algorithm) {
        int serverPort = 7777;
        String fullNameDate = "Ismael Naciri Fernandez - 04/03/2024";

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            byte[] data = fullNameDate.getBytes(StandardCharsets.UTF_8);
            byte[] hash = md.digest(data);

            return IsmaUtils.bytesToHex(hash);

        } catch (NoSuchAlgorithmException err) {
            System.out.println("Algorithm not valid!!");
        }

        return "";
    }

    public static void closeSockets() {
        try {
            if (clientSocket != null)
                clientSocket.close();
        } catch (Exception e) {
            System.out.println("Error closing the socket: " + e.getMessage());
        }
    }

}

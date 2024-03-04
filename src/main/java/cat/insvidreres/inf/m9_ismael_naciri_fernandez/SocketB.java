package cat.insvidreres.inf.m9_ismael_naciri_fernandez;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketB {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static String message;

    private static Socket ex2_2ClientSocket;
    private static int otherPort = 8888;

    public SocketB(int serverPort) {
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server is waiting for a connection on port  " + serverPort);
            clientSocket = serverSocket.accept();
            System.out.println("Connection established with client");

        } catch (IOException e) {
            System.out.println("Error  ||  " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int serverPort = 7777;
        SocketB socket = null;

        try {
            socket = new SocketB(serverPort);
            String receivedMessage = receiveMessage();
            message = receivedMessage;
            System.out.println("Paquet amb contingut:  " + receivedMessage + " enviat al socol amb adreca " + clientSocket.getInetAddress());

            uploadWithFTP(receivedMessage);
        } catch (Exception e) {
            System.out.println("Error  | " + e.getMessage());
        }
    }

    public static String receiveMessage() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String receivedMessage = in.readLine();

            return receivedMessage;
        } catch (Exception e) {
            System.out.println("Error ||  " + e.getMessage());
            return null;
        }
    }

    private static void uploadWithFTP(String message) {
        String server = "ftpupload.net";
        int port = 21;
        String username = "if0_35747537";
        String pw = "pZ7B4c2SZwD7X";

        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);

            boolean loginSuccess = ftpClient.login(username, pw);

            if (loginSuccess) {
                System.out.println("LOGIN SUCCESSFUL!");

                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                ftpClient.enterLocalPassiveMode();;
                ftpClient.changeWorkingDirectory("/processos/");
                updloadFileToServer(ftpClient, message);

            }
        } catch (Exception e) {
            System.out.println("Error  |  " + e.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected())
                    ftpClient.disconnect();
            } catch (Exception e) {
                System.out.println("Error  |  " + e.getMessage());
            }
        }
    }

    private static void updloadFileToServer(FTPClient ftpClient, String message) {

        try {
            InputStream stream = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
//            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

//            String str = br.readLine();

            boolean done = ftpClient.storeFile("IsmaelNaciriFernandez.txt", stream);

            if (done) {
                System.out.println("File IsmaelNaciriFernandez.txt uploaded correctly!");

                sendMessageToSocketC("Encripta Ismael Naciri Fernandez");
            }
        } catch (Exception e) {
            System.out.println("Error | " + e.getMessage());
        }
    }

    private static void sendMessageToSocketC(String message) {
        try {
            ex2_2ClientSocket = new Socket("localhost", otherPort);

            sendMessage(message);
        } catch (Exception e) {
            System.out.println("Error | " + e.getMessage());
        }
    }

    private static void sendMessage(String message) {
        try {
            PrintWriter out = new PrintWriter(ex2_2ClientSocket.getOutputStream(), true);
            out.println(message);
            System.out.println("Message sent successfully");
        } catch (Exception e) {
            System.out.println("Error  |  " + e.getMessage());
        }
    }
}

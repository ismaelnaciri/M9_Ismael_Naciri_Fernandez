package cat.insvidreres.inf.m9_ismael_naciri_fernandez;

import cat.insvidreres.inf.m9_ismael_naciri_fernandez.utils.IsmaUtils;
import org.apache.commons.net.ftp.FTPClient;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Scanner;

public class SocketC {

    private static ServerSocket serverSocket;
    private static Socket cliSocket;
    private static int serverPort = 8888;


    public SocketC() {
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server is waiting for a connection on port  " + serverPort);
            cliSocket = serverSocket.accept();

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
                    String fileContents = readFileData(ftpClient, "/processos/IsmaelNaciriFernandez.txt");

                    if (!fileContents.isEmpty()) {
                        System.out.println("El valor " + fileContents + " \n encriptat en RSA es " + encryptRSAGiveHex(fileContents));
                    }
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
            System.out.println("Connection established with client");
        } catch (Exception e) {
            System.out.println("Error | " + e.getMessage());
        }
    }

    private static String encryptRSAGiveHex(String message) {
        try {
            KeyPair keyPair = keyPairGenerator(512);
            PrivateKey privateKey = keyPair.getPrivate();

            byte[] messageEncrypted = encryptWithPrivateKey(message.getBytes(StandardCharsets.UTF_8), privateKey);

            return IsmaUtils.bytesToHex(messageEncrypted);

        } catch (Exception err) {
            System.out.println("ERROR sussy |  " + err.getMessage());
            return "";
        }
    }

    private static byte[] encryptWithPrivateKey(byte[] bytes, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            System.out.println("PrivateKeyEncryption ERROR  |  " + e.getMessage());
        }
        return null;
    }

    private static KeyPair keyPairGenerator(int keyLength) {
        KeyPair keyPair = null;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keyLength);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No such algorithm!!: " + e.getMessage());
        }

        return keyPair;
    }

    public static void main(String[] args) {
        SocketC socket = null;

        try {
            socket = new SocketC();
            String receivedMessage = receiveMessage();
            System.out.println("Missatge rebut: " + receivedMessage);

        } catch (Exception e) {
            System.out.println("Error   ||  " + e.getMessage());
        }
    }

    public static String receiveMessage() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliSocket.getInputStream()));
            String receivedMessage = in.readLine();

            return receivedMessage;
        } catch (Exception e) {
            System.out.println("Error ||  " + e.getMessage());
            return null;
        }
    }

    private static String readFileData(FTPClient client, String path) {
        try {
            InputStream inputStream = client.retrieveFileStream(path);
            Scanner scanner = new Scanner(inputStream);
            StringBuffer sb = new StringBuffer();

            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
                sb.append("\n");
            }
            System.out.println("Contingut del fitxer:  "  + sb);

            return sb.toString();
        } catch (Exception e) {
            System.out.println("Error |  " + e.getMessage());
            return "";
        }
    }
}

package client_side;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main extends JFrame {
    private JTextArea gameLog;

    public Main(String playerName) {
        // Initialize GUI components
        setTitle(playerName + "'s Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        gameLog = new JTextArea();
        gameLog.setEditable(false);

        // Arrange components
        setLayout(new BorderLayout());
        add(gameLog, BorderLayout.CENTER);

        setVisible(true);

        // Start the server if it's the second user
        if (playerName.equals("Player 2")) {
            startServer();
        } else {
            // If it's Player 1, try to connect to Player 2's server
            connectToServer();
        }
    }

    private void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(12345);
                System.out.println("Server started, waiting for connections...");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New connection accepted");
                    handleClient(clientSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket clientSocket) {
        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (true) {
                    String message = reader.readLine();
                    if (message == null) {
                        break; // Client disconnected
                    }
                    System.out.println("Received message: " + message);
                    updateGameLog(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 12345);
                System.out.println("Connected to server");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateGameLog(String message) {
        SwingUtilities.invokeLater(() -> gameLog.append(message + "\n"));
    }

    public static void main(String[] args) {
        // Simulate two users playing the game
        SwingUtilities.invokeLater(() -> new Main("Player 1"));
        SwingUtilities.invokeLater(() -> new Main("Player 2"));
    }
}
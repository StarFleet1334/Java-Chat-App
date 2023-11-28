package client_side;

import client_side.navbar_options.AboutOption;
import client_side.navbar_options.ContactOption;
import client_side.navbar_options.GamesOption;
import com.vdurmont.emoji.EmojiParser;
import server_side.Server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;

import java.util.ArrayList;
import java.util.Arrays;


public class ClientGui extends Thread{

    final JTextPane jtextFilDiscu = new JTextPane();
    final JTextPane jtextListUsers = new JTextPane();
    final JTextField jtextInputChat = new JTextField();
    private String oldMsg = "";
    private Thread read;
    private String serverName;
    private int PORT;
    private String name;
    PrintWriter output;

    BufferedReader input;
    Socket server;

    JTextField nameField;
    JTextField portField;
    JTextField serverField;

    public ClientGui(Socket server,JTextField nameField,JTextField portField,JTextField serverField) {
        this.serverName = serverField.getText();
        this.PORT = Integer.parseInt(portField.getText());
        this.server = server;
        this.nameField = nameField;
        this.portField = portField;
        this.serverField = serverField;

        String fontfamily = "Arial, sans-serif";
        Font font = new Font(fontfamily, Font.PLAIN, 15);

        final JFrame jfr = new JFrame("Mini-Chat-Application");
        jfr.getContentPane().setLayout(null);
        jfr.setSize(1000, 800);
        jfr.setResizable(false);
        jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Module du fil de discussion
        // width 740
        jtextFilDiscu.setBounds(25, 35, 680, 600);
        jtextFilDiscu.setFont(font);
        jtextFilDiscu.setMargin(new Insets(6, 6, 6, 6));
        jtextFilDiscu.setEditable(false);
        JScrollPane jtextFilDiscuSP = new JScrollPane(jtextFilDiscu);
        jtextFilDiscuSP.setBounds(25, 35, 680, 600);

        jtextFilDiscu.setContentType("text/html");
        jtextFilDiscu.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        jtextListUsers.setBounds(780, 35, 200, 600);
        jtextListUsers.setEditable(true);
        jtextListUsers.setFont(font);
        jtextListUsers.setMargin(new Insets(6, 6, 6, 6));
        jtextListUsers.setEditable(false);
        JScrollPane jsplistuser = new JScrollPane(jtextListUsers);
        jsplistuser.setBounds(780, 35, 200, 600);

        jtextListUsers.setContentType("text/html");
        jtextListUsers.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        // Field message user input
        jtextInputChat.setBounds(25, 650, 680, 80);
        jtextInputChat.setFont(font);
        jtextInputChat.setMargin(new Insets(6, 6, 6, 6));
        final JScrollPane jtextInputChatSP = new JScrollPane(jtextInputChat);
        jtextInputChatSP.setBounds(25, 650, 680, 80);

        // button send
        final JButton sendButton = new JButton("Send");
        sendButton.setFont(font);
        sendButton.setBounds(740, 650, 100, 35);

        // button Disconnect
        final JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setFont(font);
        disconnectButton.setBounds(840, 650, 150, 35);


        // Navbar

        JPanel navbarPanel = new JPanel();
        navbarPanel.setBackground(Color.lightGray);
        ;
        JButton aboutButton = new JButton("About");
        JButton contactButton = new JButton("Contact");
        JButton gameButton = new JButton("Games");


        // Add action listeners to the buttons
        gameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamesOption gamesOption = new GamesOption();
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutOption aboutOption = new AboutOption();
            }
        });

        contactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContactOption contactOption = new ContactOption();
            }
        });


        // Add buttons to the navbar panel
        navbarPanel.add(gameButton);
        navbarPanel.add(aboutButton);
        navbarPanel.add(contactButton);



        navbarPanel.setBounds(350,0,300,35);


        jfr.add(navbarPanel);

        jtextInputChat.addKeyListener(new KeyAdapter() {
            // send message on Enter
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }

                // Get last message typed
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    String currentMessage = jtextInputChat.getText().trim();
                    jtextInputChat.setText(oldMsg);
                    oldMsg = currentMessage;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    String currentMessage = jtextInputChat.getText().trim();
                    jtextInputChat.setText(oldMsg);
                    oldMsg = currentMessage;
                }
            }
        });

        // Click on send button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                sendMessage();
            }
        });

        jtextFilDiscu.setBackground(Color.LIGHT_GRAY);
        jtextListUsers.setBackground(Color.LIGHT_GRAY);

        // ajout des éléments

        jfr.add(jtextFilDiscuSP);
        jfr.add(jsplistuser);
        jfr.setVisible(true);

        try {
            name = nameField.getText();
            String port = portField.getText();
            serverName = serverField.getText();
            PORT = Integer.parseInt(port);

            // create new Read Thread

            appendToPane(jtextFilDiscu,"<span><h1>Welcome " + name + "</h1></span>");


            appendToPane(jtextFilDiscu, "<span>Connecting to " + serverName + " on port " + PORT + "...</span>");

            input = new BufferedReader(new InputStreamReader(server.getInputStream()));
            output = new PrintWriter(server.getOutputStream(), true);

            // send nickname to server
            output.println(name);

            read = new Read();
            read.start();
            jfr.add(jtextInputChatSP);
            jfr.add(sendButton);
            jfr.add(disconnectButton);
            jfr.revalidate();
            jfr.repaint();
            jtextListUsers.setBackground(Color.white);
        } catch (Exception ex) {
            appendToPane(jtextFilDiscu, "<span>Could not connect to Server</span>");
        }

        disconnectButton.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent ae) {
                jfr.remove(sendButton);
                jfr.remove(jtextInputChatSP);
                jfr.remove(disconnectButton);
                jfr.revalidate();
                jfr.repaint();
                read.interrupt();
                jtextListUsers.setText(null);
                jtextFilDiscu.setBackground(Color.LIGHT_GRAY);
                jtextListUsers.setBackground(Color.LIGHT_GRAY);
                appendToPane(jtextFilDiscu, "<span>Connection closed.</span>");
                output.close();
                jfr.dispose();
            }
        });

    }



    // envoi des messages
    public void sendMessage() {
        try {
            String message = jtextInputChat.getText().trim();
            if (message.equals("")) {
                return;
            }
            this.oldMsg = message;
            output.println(message);
            jtextInputChat.requestFocus();
            jtextInputChat.setText(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
    }





    // read new incoming messages
    class Read extends Thread {
        public void run() {
            String message;
            while(!Thread.currentThread().isInterrupted()){
                try {
                    message = input.readLine();
                    if(message != null){
                        if (message.charAt(0) == '[') {
                            message = message.substring(1, message.length()-1);
                            ArrayList<String> ListUser = new ArrayList<String>(
                                    Arrays.asList(message.split(", "))
                            );
                            jtextListUsers.setText(null);

                            String emoji = ":st_patrick:";
                            String result = EmojiParser.parseToUnicode(emoji);

                            appendToPane(jtextListUsers,  result + " ----- " + "Online" + " ----- " + result);
                            appendToPane(jtextListUsers," ");
                            for (String user : ListUser) {
                                appendToPane(jtextListUsers, "@ " + user);
                            }
                        }else{
                            appendToPane(jtextFilDiscu, message);
                        }
                    }
                }
                catch (IOException ex) {
                    System.err.println("Failed to parse incoming message");
                }
            }
        }
    }

    // send html to pane
    private void appendToPane(JTextPane tp, String msg){
        HTMLDocument doc = (HTMLDocument)tp.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
        try {
            editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
            tp.setCaretPosition(doc.getLength());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
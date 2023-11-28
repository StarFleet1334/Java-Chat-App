package client_side;

import models.AvailableServers;
import models.IndividualServer;
import utils.Jackson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class LoginGui {

    private static final ArrayList<ArrayList<String>> availableServers = new ArrayList<>();

    private JComboBox<String> serverDropdown;
    private JComboBox<String> portDropdown;

    private static Jackson jackson;

    static {
        try {
            jackson = new Jackson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public LoginGui() throws IOException {
        System.out.println(Jackson.individualServerList);
        for (IndividualServer x: Jackson.getIndividualServerList()) {
            availableServers.add(new ArrayList<>(Arrays.asList(x.getPort(),x.getServer())));
        }

        String fontfamily = "Arial, sans-serif";
        Font font = new Font(fontfamily, Font.PLAIN, 15);

        serverDropdown = new JComboBox<>();
        portDropdown = new JComboBox<>();

        populateServerDropdown();  // Populate the server dropdown with available servers
        populatePortDropdown();    // Populate the port dropdown with available ports



        final JFrame frame = new JFrame("Mini-Chat-Application");
        frame.getContentPane().setLayout(null);
        frame.setSize(600, 300);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.getContentPane().setBackground(Color.DARK_GRAY);

        final JLabel welcomeMessage = new JLabel("Welcome - Join the Chat");
        welcomeMessage.setForeground(Color.WHITE);
        welcomeMessage.setFont(new Font("Arial", Font.PLAIN, 30)); //Creating an Arial Font Style but with size 30


        final JLabel nickName = new JLabel("Name: ");
        nickName.setForeground(Color.white);
        final JTextField nameField = new JTextField();

        final JLabel portInfo = new JLabel("Port: ");
        portInfo.setForeground(Color.WHITE);

        final JLabel serverName = new JLabel("Server: ");
        serverName.setForeground(Color.white);

        final JButton jcbtn = new JButton("Connect");


        final JButton createServerBtn = new JButton("Create-server");

        final JButton checkServersBtn = new JButton("Check-servers");

        checkServersBtn.setFont(font);

        // check if those field are not empty
        nameField.getDocument().addDocumentListener(new TextListener(nameField, jcbtn));


        jcbtn.setFont(font);
        createServerBtn.setFont(font);

        // Welcome Message
        welcomeMessage.setBounds(120,20,350,30);


        // Name Field

        nickName.setBounds(200,60,80,30);
        nameField.setBounds(280,60,120,30);


        // Port Field

        portInfo.setBounds(200,100,80,30);
        portDropdown.setBounds(280,100,120,30);


        // Server

        serverName.setBounds(200,140,80,30);
        serverDropdown.setBounds(280,140,120,30);

        // Connect button

        jcbtn.setBounds(200,180,100,30);

        //  server button
        createServerBtn.setBounds(310,180,150,30);

        // Check Server Button
        checkServersBtn.setBounds(250,220,150,30);


        // Adding Components to Frame
        frame.add(nickName);
        frame.add(nameField);

        frame.add(checkServersBtn);

        frame.add(portInfo);
        frame.add(portDropdown);

        frame.add(serverName);
        frame.add(serverDropdown);

        frame.add(jcbtn);

        frame.add(welcomeMessage);

        frame.add(createServerBtn);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        checkServersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AvailableServers availableServers1 = new AvailableServers(jackson);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        // On connect
        jcbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    Socket server = new Socket((String) serverDropdown.getSelectedItem(), Integer.parseInt((String) Objects.requireNonNull(portDropdown.getSelectedItem())));
                    // server,nameField,portField,serverField
                    JTextField portField = new JTextField((String) portDropdown.getSelectedItem());
                    JTextField serverField = new JTextField((String) serverDropdown.getSelectedItem());
                    ClientGui clientGui = new ClientGui(server,nameField,portField,serverField);

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame,e.getMessage());
                } finally {
                    for (IndividualServer x : Jackson.individualServerList) {
                        if (x.getPort().equals(portDropdown.getSelectedItem())
                        && x.getServer().equals(serverDropdown.getSelectedItem())) {
                            x.incrementNumberOfUsersJoined();
                        }
                        System.out.println(x.getPort() + " " + x.getNumberOfUsersJoined());
                    }
                }
            }
        });

        createServerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateServerGui createServerGui = new CreateServerGui(availableServers,jackson);
            }
        });
    }
    private static void createLoginGui() {
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginGui();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 2; i++) {
            createLoginGui();
        }
    }


    public static void addServer(String server, String port) {
        ArrayList<String> serverInfo = new ArrayList<>();
        serverInfo.add(server);
        serverInfo.add(port);
        availableServers.add(serverInfo);
    }

    // Handle unique dropdrown
    private HashSet<ArrayList<String>> uniqueDropDown() {
        return new HashSet<>(LoginGui.availableServers);
    }


    private void populateServerDropdown() {
        // Clear existing items in the server dropdown
        serverDropdown.removeAllItems();

        // Populate the server dropdown with available servers
        for (ArrayList<String> serverInfo : uniqueDropDown()) {
            String server = serverInfo.get(1);
            serverDropdown.addItem(server);
        }
    }



    private void populatePortDropdown() {
        // Clear existing items in the port dropdown
        portDropdown.removeAllItems();

        // Populate the port dropdown with available ports
        // You may need to modify this based on how you obtain available ports
        // For simplicity, let's assume a predefined list of ports

        for (ArrayList<String> serverInfo : uniqueDropDown()) {
            String port = serverInfo.get(0);
            portDropdown.addItem(port);
        }
    }
}
class TextListener implements DocumentListener {
    JTextField nameField;
    JButton jcbtn;

    public TextListener(JTextField nameField,JButton jcbtn){
        this.nameField = nameField;
        this.jcbtn = jcbtn;
    }

    public void changedUpdate(DocumentEvent e) {}

    public void removeUpdate(DocumentEvent e) {
        if(nameField.getText().trim().equals("")
        ){
            jcbtn.setEnabled(false);
        }else{
            jcbtn.setEnabled(true);
        }
    }
    public void insertUpdate(DocumentEvent e) {
        if(nameField.getText().trim().equals("")
        ){
            jcbtn.setEnabled(false);
        }else{
            jcbtn.setEnabled(true);
        }
    }

}

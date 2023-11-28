package client_side;

import server_side.Server;
import utils.Jackson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class CreateServerGui {


    public CreateServerGui(ArrayList<ArrayList<String>> availableServers,Jackson jackson) {
        String fontfamily = "Arial, sans-serif";
        Font font = new Font(fontfamily, Font.PLAIN, 15);


        final JFrame frame = new JFrame("Mini-Chat-Application");
        frame.getContentPane().setLayout(null);
        frame.setSize(600, 300);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.getContentPane().setBackground(Color.DARK_GRAY);

        final JLabel welcomeMessage = new JLabel("Welcome - Create the Server");
        welcomeMessage.setForeground(Color.WHITE);
        welcomeMessage.setFont(new Font("Arial", Font.PLAIN, 30)); //Creating an Arial Font Style but with size 30


        final JLabel portInfo = new JLabel("Port: ");
        portInfo.setForeground(Color.WHITE);
        final JTextField portField = new JTextField();

        final JLabel serverName = new JLabel("Server: ");
        serverName.setForeground(Color.white);
        final JTextField serverField = new JTextField();

        final JButton createBtn = new JButton("create");

        final JButton backBnt = new JButton("go-back");



        // check if those field are not empty
        portField.getDocument().addDocumentListener(new TextListenerForServer(portField, serverField, createBtn));
        serverField.getDocument().addDocumentListener(new TextListenerForServer( portField, serverField, createBtn));


        createBtn.setFont(font);
        backBnt.setFont(font);

        // Welcome Message
        welcomeMessage.setBounds(120,40,400,30);


        // Port Field

        portInfo.setBounds(200,100,80,30);
        portField.setBounds(280,100,120,30);


        // Server

        serverName.setBounds(200,140,80,30);
        serverField.setBounds(280,140,120,30);

        // create button

        createBtn.setBounds(220,180,100,30);

        backBnt.setBounds(320,180,100,30);


        frame.add(portInfo);
        frame.add(portField);

        frame.add(serverName);
        frame.add(serverField);

        frame.add(createBtn);

        frame.add(welcomeMessage);

        frame.add(backBnt);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui.addServer(portField.getText(),serverField.getText());
                jackson.marshallingFromObjectToJson(portField.getText(),serverField.getText());
                JOptionPane.showMessageDialog(frame,"Server created and added to available servers!");
            }
        });

        backBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LoginGui loginGui = new LoginGui();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


}

class TextListenerForServer implements DocumentListener {
    JTextField portField;
    JTextField serverField;
    JButton createBtnField;

    public TextListenerForServer(JTextField portField, JTextField serverField, JButton createBtnField){
        this.portField = portField;
        this.serverField = serverField;
        this.createBtnField = createBtnField;
    }

    public void changedUpdate(DocumentEvent e) {}

    public void removeUpdate(DocumentEvent e) {
        if(portField.getText().trim().equals("") ||
                serverField.getText().trim().equals("")
        ){
            createBtnField.setEnabled(false);
        }else{
            createBtnField.setEnabled(true);
        }
    }
    public void insertUpdate(DocumentEvent e) {
        if(portField.getText().trim().equals("") ||
                serverField.getText().trim().equals("")
        ){
            createBtnField.setEnabled(false);
        }else{
            createBtnField.setEnabled(true);
        }
    }

}
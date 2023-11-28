package models;

import utils.Jackson;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AvailableServers {


    final JTextPane jtextFilDiscu = new JTextPane();

    public AvailableServers(Jackson jackson) throws IOException {
        String fontfamily = "Arial, sans-serif";
        Font font = new Font(fontfamily, Font.PLAIN, 15);


        final JFrame frame = new JFrame("Mini-Chat-Application");
        frame.getContentPane().setLayout(null);
        frame.setSize(600, 350);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Module du fil de discussion
        jtextFilDiscu.setBounds(55, 72, 500, 180);
        jtextFilDiscu.setFont(font);
        jtextFilDiscu.setMargin(new Insets(6, 6, 6, 6));
        jtextFilDiscu.setEditable(false);
        JScrollPane jtextFilDiscuSP = new JScrollPane(jtextFilDiscu);
        jtextFilDiscuSP.setBounds(55, 72, 500, 180);

        jtextFilDiscu.setContentType("text/html");
        jtextFilDiscu.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        appendToPane(jtextFilDiscu, "<h4>Available Servers</h4>"
                + "<ul>");

        for (IndividualServer x : jackson.getIndividualServerList()) {
            appendToPane(jtextFilDiscu,
                    "<li>Port: " + x.getPort() + ", Server: " + x.getServer() + ", users: " + x.getNumberOfUsersJoined() + "</li>");
        }
        appendToPane(jtextFilDiscu,"</ul><br/>");


        frame.getContentPane().setBackground(Color.DARK_GRAY);

        final JLabel welcomeMessage = new JLabel("About Option");
        welcomeMessage.setForeground(Color.WHITE);
        welcomeMessage.setFont(new Font("Arial", Font.PLAIN, 30)); //Creating an Arial Font Style but with size 30

        final JButton jButton = new JButton("Close");

        // Welcome Message
        welcomeMessage.setBounds(220,25,400,30);


        jButton.setBounds(250,260,100,40);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.add(jtextFilDiscuSP);
        frame.add(jButton);
        frame.add(welcomeMessage);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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

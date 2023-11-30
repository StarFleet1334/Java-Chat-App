package client_side.navbar_options;

import games.break_game.Gameplay;
import games.snake_game.SnakeGame;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamesOption {

    final JTextPane jtextFilDiscu = new JTextPane();

    public GamesOption() {
        String fontfamily = "Arial, sans-serif";
        Font font = new Font(fontfamily, Font.PLAIN, 15);


        final JFrame frame = new JFrame("Mini-Chat-Application");
        frame.getContentPane().setLayout(null);
        frame.setSize(600, 350);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        frame.getContentPane().setBackground(Color.DARK_GRAY);

        final JLabel welcomeMessage = new JLabel("The Possible Games: ");
        welcomeMessage.setForeground(Color.WHITE);
        welcomeMessage.setFont(new Font("Arial", Font.PLAIN, 30)); //Creating an Arial Font Style but with size 30



        final JButton snakeGameBtn = new JButton("Snake Game");
        final JButton breakGameBtn = new JButton("Break Game");
        final JButton jButton = new JButton("Close");

        // Welcome Message
        welcomeMessage.setBounds(180,25,300,30);


        breakGameBtn.setBounds(250,100,100,40);

        snakeGameBtn.setBounds(250,150,100,40);

        jButton.setBounds(250,200,100,40);

        snakeGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        breakGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                Gameplay gamePlay = new Gameplay();

                frame.setBounds(200,100, 700, 600);
                frame.setTitle("Breakout Ball");
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.add(gamePlay);
                frame.setVisible(true);
            }
        });

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });


        frame.add(breakGameBtn);
        frame.add(snakeGameBtn);
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

package server_side;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiParser;
import models.*;
import utils.Jackson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.awt.Color;

public class Server {

    private  List<User> clients;

    private ServerSocket server;

    private int port;

    private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public static void main(String[] args) throws IOException, InterruptedException {

        // Parse Json into Object
        Jackson jackson = new Jackson();

        for (IndividualServer x : Jackson.getIndividualServerList()) {
            final int port = Integer.parseInt(x.getPort());

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Server server1 = new Server(port);
                    try {
                        server1.run();
                        Thread.sleep(1000);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    public static void runSingleServer(String port) {

        System.out.println("was here");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Server server1 = new Server(Integer.parseInt(port));
                try {
                    System.out.println("Inside");
                    server1.run();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<User>();
    }


    public void run() throws IOException {
        server = new ServerSocket(port) {
            protected void finalize() throws IOException {
                this.close();
            }
        };

        System.out.println("Port " + port + " is now opened.");

        while (true) {
            // accepts a new client
            Socket client = server.accept();

            // get nickname of newUser
            String nickname = (new Scanner ( client.getInputStream() )).nextLine();
            nickname = nickname.replace(",", ""); //  ',' use for serialisation
            nickname = nickname.replace(" ", "_");
            System.out.println("New Client: \"" + nickname + "\"\n\t     Host:" + client.getInetAddress().getHostAddress());

            // create new User
            User newUser = new User(client, nickname);

            // add newUser message to list
            this.clients.add(newUser);

            // Welcome msg
            newUser.getOutStream().println(
                    "<img src='https://www.kizoa.fr/img/e8nZC.gif' height='42' width='42'>"
                            + "<b>Welcome</b> " + newUser.toString() +
                            "<img src='https://www.kizoa.fr/img/e8nZC.gif' height='42' width='42'>"
            );

            // create a new thread for newUser incoming messages handling
            new Thread(new UserHandler(this, newUser)).start();
        }
    }

    // delete a user from the list
    public void removeUser(User user){
        this.clients.remove(user);
    }

    // send incoming msg to all Users
    public void broadcastMessages(String msg, User userSender) {
        for (User client : this.clients) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            client.getOutStream().println(
                    userSender.toString() + "<span>: " + sdf3.format(timestamp) + " " + msg + "</span>");
        }
    }

    // send list of clients to all Users
    public void broadcastAllUsers(){
        for (User client : this.clients) {
            client.getOutStream().println(this.clients);
        }
    }

    // send message to a User (String)
    public void sendMessageToUser(String msg, User userSender, String user){
        boolean find = false;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (User client : this.clients) {
            if (client.getNickname().equals(user) && client != userSender) {
                find = true;
                userSender.getOutStream().println(userSender.toString() + " -> " + client.toString() +": " + msg);
                client.getOutStream().println(
                        "(<b>Private</b>)" + userSender.toString() + " " + sdf3.format(timestamp)  + "<span>: " + msg+"</span>");
            }
        }
        if (!find) {
            userSender.getOutStream().println(userSender.toString() + " -> (<b>no one!</b>): " + msg);
        }
    }
}

class UserHandler implements Runnable {

    private Server server;
    private User user;

    public UserHandler(Server server, User user) {
        this.server = server;
        this.user = user;
        this.server.broadcastAllUsers();
    }

    public void run() {
        String message;

        // when there is a new message, broadcast to all
        Scanner sc = new Scanner(this.user.getInputStream());
        while (sc.hasNextLine()) {
            message = sc.nextLine();

            File file = new File(
                    Objects.requireNonNull(this.getClass().getClassLoader().getResource("emoji.json")).getFile()
            );
            ObjectMapper mapper = new ObjectMapper();

            // Read the JSON data into a List<Colors>
            List<Emoji> emojiList = null;
            try {
                emojiList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Emoji.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // smiley
            for (Emoji x : emojiList) {
                message = message.replace(x.getEmoji(),x.getUrl());
            }
            // Gestion des messages private
            if (message.charAt(0) == '@'){
                if(message.contains(" ")){
                    System.out.println("private msg : " + message);
                    int firstSpace = message.indexOf(" ");
                    String userPrivate= message.substring(1, firstSpace);
                    server.sendMessageToUser(
                            message.substring(
                                    firstSpace+1, message.length()
                            ), user, userPrivate
                    );
                }

                // Gestion du changement
            }else if (message.charAt(0) == '#'){
                user.changeColor(message);
                // update color for all other users
                this.server.broadcastAllUsers();
            }else{
                // update user list
                server.broadcastMessages(message, user);
            }
        }
        // end of Thread
        server.removeUser(user);
        this.server.broadcastAllUsers();
        sc.close();
    }
}



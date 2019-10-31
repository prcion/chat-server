package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends JFrame implements Runnable {
    String loginName;
    JTextArea messages;
    JTextField sendMessages;

    JButton send;
    JButton logout;

    DataInputStream in;
    DataOutputStream out;

    private void logout(){
        try {
            out.writeUTF(loginName + " LOGOUT");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(){
        try{
            if(sendMessages.getText().length() > 0)
                out.writeUTF(loginName + " DATA " + sendMessages.getText());
            sendMessages.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChatClient(String loginName) throws UnknownHostException, IOException {
        super(loginName);
        this.loginName = loginName;

        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                logout();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });

        messages = new JTextArea(18,50);
        sendMessages = new JTextField(50);
        messages.setEditable(false);
        send = new JButton("Send");
        logout = new JButton("Logout");

        send.addActionListener(event->{
            send();
        });
        logout.addActionListener(event ->{
            logout();
        });

        sendMessages.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
                    send();
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
        Socket socket = new Socket("127.0.0.1", 4444);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        out.writeUTF(loginName);
        out.writeUTF(loginName + " LOGIN");

        setUp();
    }

    private void setUp(){
        setSize(600, 400);
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(messages));
        panel.add(sendMessages);
        panel.add(send);
        panel.add(logout);
        add(panel);
        new Thread(this).start();
        setVisible(true);
    }

    @Override
    public void run() {
        while (true){
            try{
                messages.append("\n" + in.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package client;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Client extends JFrame implements ActionListener {

    static Sender sender;
    static Receiver receiver;
    static Socket client=null;
    JLabel ipLable = new JLabel("IP:");
    JTextField ipText = new JTextField("127.0.0.1");
    JLabel portLable = new JLabel("端口:");
    JTextField portText = new JTextField("11451");
    JLabel nameLable = new JLabel("昵称:");
    JTextField nameText = new JTextField();
    static JButton enterRoom = new JButton("进入聊天室");
    static JButton exitRoom = new JButton("退出聊天室");
    static JTextArea chatArea = new JTextArea("欢迎使用本聊天室");
    static JTextArea inputArea =new JTextArea();
    static JButton sendButton = new JButton("发送");


    Client(){
        setTitle("课设-聊天室 by Kadee Young");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        JPanel box1=new JPanel(new GridLayout(1,8));
        box1.setPreferredSize(new Dimension(getWidth(),30));
        box1.add(ipLable);
        box1.add(ipText);
        box1.add(portLable);
        box1.add(portText);
        box1.add(nameLable);
        box1.add(nameText);
        box1.add(enterRoom);
        box1.add(exitRoom);
        add(box1,BorderLayout.NORTH);


        chatArea.setFont(new Font("宋体",Font.PLAIN,18));
        setFont(new Font("宋体",Font.PLAIN,14));
        JPanel box2 = new JPanel(new GridLayout(1,1));
        box2.setPreferredSize(new Dimension(getWidth(),325));
        chatArea.setSize(new Dimension(getWidth(),325));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatArea.setEditable(false);

// 设置滚动面板的样式和行为
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

// 将滚动面板添加到box2
        box2.add(scrollPane);

        add(box2);

        JPanel box3 = new JPanel(new BorderLayout());
        box3.setPreferredSize(new Dimension(getWidth(),60));

        inputArea.setPreferredSize(new Dimension(420,300));
        inputArea.setBorder(BorderFactory.createLineBorder(Color.black));
        JScrollPane scrollPane1 = new JScrollPane(inputArea);
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sendButton.setPreferredSize(new Dimension(150,50));
        box3.add(Box.createRigidArea(new Dimension(0, 10)),BorderLayout.NORTH);

        box3.add(scrollPane1,BorderLayout.WEST);

        box3.add(Box.createRigidArea(new Dimension(30, 0)));
        box3.add(sendButton,BorderLayout.EAST);
        add(box3,BorderLayout.SOUTH);

        enterRoom.addActionListener(this);
        exitRoom.addActionListener(this);
        sendButton.addActionListener(this);

        exitRoom.setEnabled(false);
        sendButton.setEnabled(false);

        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(exitRoom.isEnabled()){
                    sender.enqueueMessage("退出了聊天室");
                    // exitRoom.doClick();
                    try {
                        sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (receiver.NameJudge()){
                        
                        sender.stopRun();
                        receiver.stopRun();
                        try {
                            client.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        enterRoom.setEnabled(true);
                        exitRoom.setEnabled(false);
                        sendButton.setEnabled(false);
                        receiver.namejud=false;
                    }
                }

                super.windowClosing(e);
            }
        });
    }

    public static void main(String[] args) {
        new Client();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();
        if(source==enterRoom){

            try {
                // System.out.println("尝试");
                client =new Socket("127.0.0.1",11451);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(chatArea, "服务器未启动!");
                return ;
            }
            sender = new Sender(client,nameText.getText());
            receiver = new Receiver(client);
            sender.start();
            receiver.start();
            sender.enqueueMessage("name:" + sender.getuserName());

            enterRoom.setEnabled(false);
            exitRoom.setEnabled(true);
            sendButton.setEnabled(true);
            if (receiver.NameJudge()){
                JOptionPane.showMessageDialog(this, "昵称重复!");
                sender.stopRun();
                receiver.stopRun();
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                enterRoom.setEnabled(true);
                exitRoom.setEnabled(false);
                sendButton.setEnabled(false);
                receiver.namejud=false;
            }




        } else if (source==sendButton) {
            if(client==null){
                JOptionPane.showMessageDialog(this, "尚未登陆!");
            }else {
                sender.enqueueMessage("发来了一条消息:"+inputArea.getText());
                inputArea.setText("");
            }
        } else if (source == exitRoom) {
            sender.enqueueMessage("退出了聊天室");
            // exitRoom.doClick();
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            if (receiver.NameJudge()){
                // System.out.println("if");
                JOptionPane.showMessageDialog(this, "你退出了聊天室!");
                sender.stopRun();
                receiver.stopRun();
                try {
                    client.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                enterRoom.setEnabled(true);
                exitRoom.setEnabled(false);
                sendButton.setEnabled(false);
                receiver.namejud=false;
            }
        }

    }
}

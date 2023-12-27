package server;
//用于巡检。

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Inspection extends Thread{
    boolean INS_START=true;
    public static BlockingQueue<ClientHandler> ExitQueue=new ArrayBlockingQueue<>(20);
    public static BlockingQueue<CliAndMsg> CM=new ArrayBlockingQueue<>(20);
    @Override
    public void run(){

            new Thread(() -> {
                while (INS_START){
                    try {
                        //System.out.println("try1");
                        CliAndMsg t = CM.take();
                       // System.out.println("try2");
                        if (t.cli.clientSocket.isClosed()){
                            ExitQueue.add(t.cli);
                        }else {
                            processMSg(t);
                        }

                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

            new Thread(() -> {
                while (INS_START){
                    ClientHandler c = null;
                    try {
                        c = ExitQueue.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        Exit_someone(c);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();




    }
    public void Exit_someone(ClientHandler client) throws IOException {

        client.closeConnection();
    }
    void exit_p(CliAndMsg t) throws IOException {
        for (ClientHandler p: Anteroom.people){
            if(p.clientSocket.equals(t.cli.clientSocket))
            {

                p.sender.send("serversays:name重复");
                p.receiver.stopRun();
                ExitQueue.add(p);
                Anteroom.people.remove(p);
            //    System.out.println("exit进队");
                break;
            }
        }
    }
    public void processMSg(CliAndMsg t) throws InterruptedException, IOException {

        String msg=t.getMsg();
        if(msg.contains("发来了一条消息:")){
            for (ClientHandler p: Anteroom.people){
                Date date =new Date();
                SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
                String dt=dateFormat.format(date);
                String s = "[" +dt + " " +t.cli.name + msg.replaceAll("发来了一条消息","]");
                p.sender.send(s);
            }
        } else if (msg.contains("退出了聊天室")) {
            exit_p(t);
            for (ClientHandler p: Anteroom.people){
                Date date =new Date();
                SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
                String dt=dateFormat.format(date);
                String s = "[" +dt + "]" + ' '+ t.cli.name + " "+ msg;

                p.sender.send(s);
            }

        } else if (msg.contains("name:")) {
            boolean flag=false;
            for (ClientHandler p: Anteroom.people){
                String name = msg.replaceAll("name:","");
                if (name.equals(p.name)){
                    flag=true;
                    break;
                }
            }
            if(!flag){
             //   System.out.println("!flag");
                t.cli.name=msg.replaceAll("name:","");
                for (ClientHandler p: Anteroom.people){
                    if(p.clientSocket.equals(t.cli.clientSocket)){
                    //    System.out.println("名字匹配成功");
                        p.name=t.cli.name;
                      //  System.out.println(p.name);
                        break;
                    }
                }
                for (ClientHandler p: Anteroom.people){
                    Date date =new Date();
                    SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
                    String dt=dateFormat.format(date);
                    String s = "[" +dt + "]" + t.cli.name + "进入了房间";
                    p.sender.send(s);
                }
            }else {
                //name重复
            //    System.out.println("name");
                String name = msg.replaceAll("name:","");
                exit_p(t);


            }

        }

    }

}

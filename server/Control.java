package server;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Control extends Thread {
    boolean START = true;
    @Override
    public void run(){
        while (START){
            System.out.println("\n请输入指令：\nend--结束程序；\ncount--聊天者数量；\nchatters--列出所有聊天者；\nkickout+空格+昵称 踢出聊天室");
            Scanner scanner = new Scanner(System.in);
            String next = scanner.nextLine();
//            System.out.println(next);
            if (next.equals("end")) {
                Iterator<ClientHandler> iterator = Anteroom.people.iterator();
                while (iterator.hasNext()) {
                    ClientHandler p = iterator.next();
                    try {
                        p.sender.send("聊天室结束，江湖有缘再见!");
                        p.sender.send("serversays:服务器终止");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    p.receiver.stopRun();
                    Inspection.ExitQueue.add(p);
                    iterator.remove(); // 使用迭代器的 remove 方法删除元素
                }
                System.exit(0);

            } else if (next.equals("count"))
                System.out.println("聊天者数量：" + Anteroom.people.size());
            else if (next.equals("chatters")) {
                System.out.println("所有的聊客：");
                for (ClientHandler p: Anteroom.people) {
                    System.out.println(p.getsName());
                }
                System.out.println();
            } else if (next.startsWith("kickout ")) {
                String kickedName = next.substring("kickout ".length());
                kickedName = kickedName.trim();
                boolean flag = false;
                for (ClientHandler p: Anteroom.people){
                    if(p.name.equals(kickedName))
                    {

                        try {
                            p.sender.send("serversays:你被踢出去了!");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        p.receiver.stopRun();
                        Inspection.ExitQueue.add(p);
                        Anteroom.people.remove(p);
                      //  System.out.println("exit进队");
                        flag=true;
                        break;
                    }
                }
                for (ClientHandler p: Anteroom.people){
                        try {
                            p.sender.send(kickedName + " 被踢出了聊天室");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                }
                if (flag) {
                    System.out.println("踢出成功!");
                } else {
                    System.out.println("昵称不存在！");
                }

            }else{
                System.out.println("指令有误,请检查后重新输入!");
            }
        }
    }
}
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ChatClient {
    public static void main(String [] args){
        String currNick = "paijo";
        boolean stop = false;
        ChatService chatService = null;
        try {
            chatService = new ChatService("localhost");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return;
        }

        do{
            System.out.print("Command: ");
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            String[] splited = str.split("\\s+");
            if (splited[0].equals("/NICK")){
                if (splited.length != 2){
                    System.out.println("Usage: /NICK <nickname>");
                } else {
                    currNick = splited[1];
                    try {
                        chatService.nick(currNick);
                        System.out.println("[OK] Nickname changed to '"+currNick+"'");
                    } catch (IOException e) {
                        System.out.println("[ERROR] "+e.getMessage());
                    }

                }

            } else if (splited[0].equals("/JOIN")){
                if (splited.length != 2){
                    System.out.println("Usage: /JOIN <nickname>");
                } else {
                    try {
                        chatService.join(currNick,splited[1]);
                        System.out.println("[OK] "+currNick+" has joined "+splited[1]);
                    } catch (IOException e) {
                        System.out.println("[ERROR] "+e.getMessage());
                    }
                }
            } else if (splited[0].equals("/LEAVE")){
                if (splited.length != 2){
                    System.out.println("Usage: /LEAVE <channel>");
                } else {
                    try {
                        chatService.leave(currNick,splited[1]);
                        System.out.println("[OK] "+currNick+" leave "+splited[1]);
                    } catch (IOException e) {
                        System.out.println("[ERROR] "+e.getMessage());
                    }
                }
            } else if (splited[0].equals("/EXIT")){
                stop = true;
            } else {
                StringBuffer message = new StringBuffer();
                if (splited[0].startsWith("@")){
                    if (splited.length < 2){
                        System.out.println("Usage: @<channel> <text>");
                    } else {
                        String channelName = splited[0].substring(1);
                        for(int i = 1; i < splited.length; i++){
                            if (i > 1) message.append(" ");
                            message.append(splited[i]);
                        }
                        try {
                            chatService.send(channelName,currNick,message.toString());
                        } catch (IOException e) {
                            System.out.println("[ERROR] "+e.getMessage());
                        }
                    }
                } else {
                    for(int i = 0; i < splited.length; i++){
                        if (i > 0) message.append(" ");
                        message.append(splited[i]);
                    }
                    try {
                        chatService.broadcast(currNick,message.toString());
                    } catch (IOException e) {
                        System.out.println("[ERROR] " + e.getMessage());
                    }
                }
            }
            if (!stop){
                List<String> messages = chatService.takeMessages();
                for(String m:messages){
                    System.out.println(m);
                }
            }
        } while (!stop);
        System.out.println("bye");
    }
}

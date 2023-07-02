package Server.view;

import Server.controller.GroupChatController;
import Server.model.ChatMessage;
import Server.model.GroupChat;
import Server.view.GlobalChatCommands;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;

public class PrivateChatMenu {
    private AuthenticatedDataOutputStream dataOutputStream;
    private AuthenticatedDataInputStream dataInputStream;
    private User currentUser;
    private GroupChatController groupChatController;
    private GroupChat groupChat;

    public PrivateChatMenu(AuthenticatedDataOutputStream dataOutputStream,
                           AuthenticatedDataInputStream dataInputStream,
                           User currentUser) {
        this.currentUser = currentUser;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        groupChatController = new GroupChatController();

    }
    public void privateChat(){
        try {
            preSetPrivateChat();
            GroupChatMenu groupChatMenu = new GroupChatMenu(dataOutputStream, dataInputStream, currentUser);
            groupChatMenu.chatWithAGroupChat(groupChat);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void preSetPrivateChat() throws IOException {
        User targetUser = null;
        while (true){
            dataOutputStream.writeUTF("enter target user's username");
            targetUser = groupChatController.getUserByUsername(dataInputStream.readUTF());
            if (targetUser!=null) break;
            else dataOutputStream.writeUTF("enter a valid username");

        }
        dataOutputStream.writeUTF("user : " + targetUser.getUsername() +" is selected");
        HashSet<User> usersSet = new HashSet<>();
        usersSet.add(currentUser);
        usersSet.add(targetUser);
        groupChat = groupChatController.getGroupChatBySet(usersSet);
    }
}

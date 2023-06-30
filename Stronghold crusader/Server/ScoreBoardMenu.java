package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreBoardMenu {
    private DataOutputStream dataOutputStream;

    public ScoreBoardMenu(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void scoreBoard() {
        StringBuilder str = new StringBuilder();
        ArrayList<ArrayList<String>> usersInformation = ScoreBoardController.getUsersInformation();
        str.append("| rank | username | score | last entrance |\n");
        for (ArrayList<String> aUserInformation : usersInformation){
            str.append("|");
            for (String information : aUserInformation){
                str.append(" ").append(information).append(" |");
            }
            str.append("\n");
        }
        try {
            dataOutputStream.writeUTF(str.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

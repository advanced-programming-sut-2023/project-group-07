package Server.view;

import Server.controller.ScoreBoardController;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreboardPrinter extends Thread {
    private boolean keepPrinting = true;
    private DataOutputStream dataOutputStream;

    public ScoreboardPrinter(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                while (keepPrinting) {
                    dataOutputStream.writeUTF(getScoreboardInString());
                    wait(10 * 1000);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPrinting() {
        synchronized (this) {
            keepPrinting = false;
            notify();
        }
    }
    public void printNow() {
        synchronized (this) {
            notify();
        }
    }

    private static String getScoreboardInString() {
        StringBuilder str = new StringBuilder();
        ArrayList<ArrayList<String>> usersInformation = ScoreBoardController.getUsersInformation();
        str.append("| rank | username | score | last entrance |\n");
        for (ArrayList<String> aUserInformation : usersInformation) {
            str.append("|");
            for (String information : aUserInformation) {
                str.append(" ").append(information).append(" |");
            }
            str.append("\n");
        }
        return str.toString();
    }

}

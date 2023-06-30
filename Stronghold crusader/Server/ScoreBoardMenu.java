package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreBoardMenu extends Thread {
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private boolean isRunning = true;

    public ScoreBoardMenu(DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
    }

    public void run() {
        ScoreboardPrinter printScoreboard =new ScoreboardPrinter(dataOutputStream);
        printScoreboard.start();
        while (true) {
            String input = null;
            try {
                input = dataInputStream.readUTF();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (input.equals("exit")) {
                printScoreboard.stopPrinting();
                return;
            }
        }
    }


}


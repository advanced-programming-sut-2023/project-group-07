package Server.view;

import java.io.IOException;

public class ScoreBoardMenu extends Thread {
    private AuthenticatedDataOutputStream dataOutputStream;
    private AuthenticatedDataInputStream dataInputStream;
    private boolean isRunning = true;

    public ScoreBoardMenu(AuthenticatedDataOutputStream dataOutputStream,
                          AuthenticatedDataInputStream dataInputStream) {
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
            }else if (input.equals("refresh")){
                printScoreboard.printNow();
            }
        }
    }


}


package Server.view;

import model.Map;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShareMapsMenu {
    private AuthenticatedDataInputStream dataInputStream;
    private AuthenticatedDataOutputStream dataOutputStream;
    private ArrayList<Map> sharedMaps;
    private User currentUser;
    public ShareMapsMenu(AuthenticatedDataInputStream dataInputStream, AuthenticatedDataOutputStream dataOutputStream, User currentUser) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        sharedMaps = Map.getSharedMaps();
        this.currentUser = currentUser;
    }

    public void run() throws IOException {
        while (true) {
            dataOutputStream.writeUTF("SHARE MAPS MENU\n1. share your maps\n2. add shared maps");
            String input = dataInputStream.readUTF();
            if (input.equals("exit"))
                return;
            else if (input.equals("1"))
                shareMaps();
            else if (input.equals("2"))
                addMaps();
            else
                dataOutputStream.writeUTF("Invalid command!");
        }
    }


    private void addMaps() throws IOException {
        ArrayList<Map> sharedMaps = Map.getSharedMaps();
        while (true) {
            dataOutputStream.writeUTF("These are other users shared maps for you! enter the map number you want to check out our add to your maps:");
            for (int i = sharedMaps.size() - 1 ; i >= 0 ; i--)
                if (sharedMaps.get(i).getOwnersUsernames().contains(currentUser.getUsername()))
                    sharedMaps.remove(i);
            for (int i = 0 ; i < sharedMaps.size() ; i++) {
                Map map = sharedMaps.get(i);
                dataOutputStream.writeUTF((i + 1) + ". " + map.getName() + " (size: " + map.getSize() + ") (number of players: " + map.getNumberOfPlayers() + ")");
            }
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.equals("back"))
                    return;
                else if (!input.matches("\\d+") || Integer.parseInt(input) < 1 || Integer.parseInt(input) > sharedMaps.size())
                    dataOutputStream.writeUTF("Enter a whole number between 1 and " + sharedMaps.size());
                else {
                    runAddMap(input, sharedMaps);
                    break;
                }
            }

        }
    }

    private void runAddMap(String input, ArrayList<Map> sharedMaps) throws IOException {
        Map map = sharedMaps.get(Integer.parseInt(input) - 1);
        while (true) {
            dataOutputStream.writeUTF("map " + map.getName() + "\n1. checkout the map\n2. add map to your maps");
            input = dataInputStream.readUTF();
            if (input.equals("back"))
                return;
            else if (input.equals("1")) {
                MapMenuServer mapMenuServer = new MapMenuServer(dataInputStream, dataOutputStream, null);
                dataOutputStream.writeUTF("Now you can checkout map " + map.getName() + "!");
                mapMenuServer.run(map);
            }
            else if (input.equals("2")) {
                map.getOwnersUsernames().add(currentUser.getUsername());
                int index = Map.getMaps().indexOf(map);
                Map.changeMaps(map, index);
                dataOutputStream.writeUTF("map " + map.getName() + " added to your maps successfully!");
                break;
            }
            else
                dataOutputStream.writeUTF("Invalid command!");
        }
    }

    private void shareMaps() throws IOException {
        ArrayList<Map> allMaps = Map.getMaps();
        ArrayList<Map> currentUserMaps = new ArrayList<>();
        for (Map map : allMaps)
            if (map.getOwnersUsernames().contains(currentUser.getUsername()))
                currentUserMaps.add(map);
        while (true) {
            dataOutputStream.writeUTF("These are your unshared maps! enter the map number you want to share with other users:");
           for (int i = currentUserMaps.size() - 1; i>=0 ; i--)
               if (currentUserMaps.get(i).isShared())
                   currentUserMaps.remove(i);
           for (int i = 0; i < currentUserMaps.size(); i++) {
               Map map = currentUserMaps.get(i);
               dataOutputStream.writeUTF((i + 1) + ". " + map.getName() + " (size: " + map.getSize() + ") (number of players: " + map.getNumberOfPlayers() + ")");
           }
           while (true) {
               String input = dataInputStream.readUTF();
               if (input.matches("back"))
                   return;
               else if (!input.matches("\\d+") || Integer.parseInt(input) < 1 || Integer.parseInt(input) > currentUserMaps.size())
                   dataOutputStream.writeUTF("Enter a whole number between 1 and " + currentUserMaps.size());
               else {
                   Map toBeAdded = currentUserMaps.get(Integer.parseInt(input) - 1);
                   Map.getSharedMaps().add(toBeAdded);
                   toBeAdded.share();
                   Map.updateMaps();
                   dataOutputStream.writeUTF("map " + toBeAdded.getName() + " successfully added to shared maps!");
                   break;
               }
           }
        }
    }

}

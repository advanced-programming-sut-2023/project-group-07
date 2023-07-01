//package Server;
//
//import model.Map;
//import model.User;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class ShareMapsMenu {
//    private DataInputStream dataInputStream;
//    private DataOutputStream dataOutputStream;
//    private ArrayList<Map> sharedMaps;
//    private User currentUser;
//    public ShareMapsMenu(DataInputStream dataInputStream, DataOutputStream dataOutputStream, User currentUser) {
//        this.dataInputStream = dataInputStream;
//        this.dataOutputStream = dataOutputStream;
//        sharedMaps = Map.getSharedMaps();
//        this.currentUser = currentUser;
//    }
//
//    public void run() throws IOException {
//        while (true) {
//            dataOutputStream.writeUTF("SHARE MAPS MENU\n1. share your maps\n2. add shared maps");
//            String input = dataInputStream.readUTF();
//            if (input.equals("exit"))
//                return;
//            else if (input.equals("1"))
//                shareMaps();
//            else if (input.equals("2"))
//                addMaps();
//            else
//                dataOutputStream.writeUTF("Invalid command!");
//        }
//    }
//
//    private void addMaps() {
//    }
//
//    private void shareMaps() throws IOException {
//        ArrayList<Map> allMaps = Map.getMaps();
//        ArrayList<Map> currentUserMaps = new ArrayList<>();
//        for (Map map : allMaps)
//            if (map.getOwnersUsernames().contains(currentUser.getUsername()))
//                currentUserMaps.add(map);
//        while (true) {
//            dataOutputStream.writeUTF("These are your unshared maps! enter the map number you want to share with other users!");
//           for (int i = currentUserMaps.size() - 1; i>=0 ; i--)
//               if (Map.getSharedMaps().contains(currentUserMaps.get(i)))
//                   currentUserMaps.remove(i);
//           for (int i = 0; i < currentUserMaps.size(); i++) {
//               Map map = currentUserMaps.get(i);
//               dataOutputStream.writeUTF((i + 1) + ". " + map.getName() + " (size: " + map.getSize() + ") (number of players: " + map.getNumberOfPlayers() + ")\n");
//           }
//           while (true) {
//               String input = dataInputStream.readUTF();
//               if (input.matches("back"))
//                   return;
//               else if (!input.matches("\\d+") || Integer.parseInt(input) < 1 || Integer.parseInt(input) > currentUserMaps.size())
//                   dataOutputStream.writeUTF("Enter a whole number between 1 to " + currentUserMaps.size());
//               else {
//                   Map toBeAdded = currentUserMaps.get(Integer.parseInt(input) - 1);
//                   Map.getSharedMaps().add(toBeAdded);
//                   dataOutputStream.writeUTF("map " + toBeAdded.getName() + " successfully added to shared maps!");
//                   break;
//               }
//           }
//        }
//    }
//
//}

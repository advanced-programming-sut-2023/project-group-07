import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import controller.Controller;
import controller.LoginMenuController;
import model.Map;
import model.RecoveryQuestion;
import model.User;
import view.LoginMenu;
import view.Colors;
import view.CreateMapMenu;
import UnitTest.LoginMenuTest;
public class Main {
    
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
        // Game game = new Game(100);
        // Controller.currentGame = game;
        // MapMenu mn = new MapMenu(30 , 40);
        // mn.run(new Scanner(System.in));
        // CreateMapMenu cm = new CreateMapMenu();
        // cm.run(new Scanner(System.in));
        System.out.print(Colors.YELLOW_BOLD);
        System.out.print("Loading maps and users...");
        User.loadUsers();
        Map.loadMaps();
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        System.out.print(Colors.RESET);
        
        // Map map = Map.getMaps().get(1);
        // ArrayList<int[]> path = new ArrayList<>();
        // ArrayList<ArrayList<ArrayList<int[]>>> parent = new ArrayList<ArrayList<ArrayList<int[]>>>();
        // for(int i=0;i<map.getSize();i++){
        //     parent.add(new ArrayList<>());
        //     for(int j=0;j<map.getSize();j++){
        //         parent.get(i).add(new ArrayList<>());
        //     }
        // }
        // map.bfs(0, 0, parent);
        // map.findPath(path, parent, new int[]{199,199});
        // for(int[] s : path){
        //     System.out.println();
        //     System.out.println(s[0]+","+s[1]);
        // }
        
        // GameMenu gameMenu = new GameMenu();
        // gameMenu.run(new Scanner(System.in));
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.run(new Scanner(System.in));
    
    }
    
}

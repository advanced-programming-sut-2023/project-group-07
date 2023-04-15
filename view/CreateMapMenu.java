package view;
import java.util.Scanner;
import controller.CreateMapMenuController;
import controller.MapMenuCommands;
public class CreateMapMenu extends MapMenu{
    private final CreateMapMenuController controller = new CreateMapMenuController();
    
    public 
    public void run(Scanner scanner){
        while(true){
            String input = scanner.nextLine();
            //if(CreateMapMenuCommands.getMatcher(input, CreateMapMenuCommands.SET_PIXEL_TEXTURE)!=null)

        }
    }
    
}
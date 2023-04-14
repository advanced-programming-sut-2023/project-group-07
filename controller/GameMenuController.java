package controller;

import java.util.ArrayList;

<<<<<<< HEAD
import model.Game;
=======
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af
import model.Government;
import model.Map;
import model.User;
public class GameMenuController {
<<<<<<< HEAD
    Game game = new Game();
}
=======
    private User currentUser;
    private ArrayList<Government> governments = new ArrayList<>();
    private Map map;
    public Messages taxRate(int rate,Government government){
        if(rate<-3 || rate>8) return Messages.INVALID_RATE;
        if(rate<0){
            government.setTaxAmount(1-(rate+3)*0.2);
            government.setTaxPopularity(7-(rate+3)*2);
        }
        else if(rate==0){
            government.setTaxAmount(0);
            government.setTaxPopularity(1);
        }
        else{
            government.setTaxAmount(0.6+(rate-1)*0.2);
            if(rate<5)
                government.setTaxPopularity(rate*2);
            else
                government.setTaxPopularity((rate-2)*4);
        }
        return Messages.RATE_CHANGE_SUCCESSFULL;
    }
    public ArrayList<Government> getGovernments() {
        return governments;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void endOfTurn(){
        for(Government government : getGovernments()){
            government.setPopularity(government.getPopularity()+government.getTaxPopularity());
            government.setGold(government.getGold()+government.getTaxAmount()*government.getPopulation());
        }
    }
    
}
>>>>>>> 95362dc23a1e9e7afd19cb23842b424c73a9b6af

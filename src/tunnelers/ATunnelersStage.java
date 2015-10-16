package tunnelers;

import javafx.stage.Stage;
import tunnelers.structure.Settings;

/**
 *
 * @author Stepan
 */
public abstract class ATunnelersStage extends Stage{
    
    protected static Settings settings;
    protected static long updateCounter;
    
    public static final int CLOSE = 1,
                            CHANGE_TO_MENU = 2,
                            CHANGE_TO_GAME = 4;
    
    protected int returnValue = 0;
    
    public abstract void update();
    
    public ATunnelersStage(){
        updateCounter = 0;
        settings = Settings.getInstance();
    }
    
    
    public abstract void changeScene(Class scene);
    
    public void exit(){
        this.returnValue = CLOSE;
        this.close();
    }
    
    public int getReturnCode(){
        return returnValue;
    }
    
}

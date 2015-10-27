package tunnelers;

import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;

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
    
    public void exit(){
        this.exit(CLOSE);
    }
    
    public void exit(int exitValue){
        this.returnValue = exitValue;
        this.close();
    }
    
    public int getReturnCode(){
        return returnValue;
    }
    
    protected void changeScene(ATunnelersScene scene){
        this.setScene(scene);
        this.setTitle(String.format("%s %s %s",settings.getGameName(), settings.getTitleSeparator(), scene.getName()));
    }
    
    public final void changeScene(Class reqScene){
        ATunnelersScene scene = classToInstance(reqScene);
        if(scene == null){
            return;
        }
        changeScene(scene);
    }
    
    protected ATunnelersScene classToInstance(Class scene){
        if(scene == null){
            return null;
        }
        if(!ATunnelersScene.class.isAssignableFrom(scene)){
            System.out.format("%s not assignable from %s", scene.getSimpleName(), ATunnelersScene.class.getSimpleName());
            return null;
        }
        try{
            return invokeInstance(scene);
        } catch (IllegalAccessException | NoSuchMethodException | 
                IllegalArgumentException | InvocationTargetException e){
            System.err.println("Couldn't get instance of new scene...");
            e.printStackTrace();
        }
        return null;        
    }
    
    protected ATunnelersScene invokeInstance(Class scene) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        return (ATunnelersScene)scene.getDeclaredMethod("getInstance").invoke(null);
    }
    
    
}

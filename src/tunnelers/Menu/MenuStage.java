package tunnelers.Menu;

import java.lang.reflect.InvocationTargetException;
import tunnelers.ATunnelersStage;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class MenuStage extends ATunnelersStage {

    private static MenuStage instance;
    
    private NetWorks returnNW;
    
    private MenuStage() {
        super();
        this.changeScene(MainMenuScene.class);
    }
    
    public static MenuStage getInstance(){
        if(instance == null){
            instance = new MenuStage();
        }        
        return instance;
    }
    
    @Override
    public void update(){
        updateCounter++;
        if(updateCounter % settings.getDelay() == 0){
            System.out.printf("Update count: %d\n", updateCounter / settings.getDelay());
        }
    }
    
    protected void prevScene(){
        AMenuScene scene = (AMenuScene)this.getScene();
        this.changeScene(scene.getPrevScene());
    }
    
    @Override
    public final void changeScene(Class reqScene){
        AMenuScene scene = classToInstance(reqScene);
        if(scene == null){
            return;
        }
        changeScene(scene);
    }
    
    protected void changeScene(AMenuScene scene){
        this.setScene(scene);
        this.setTitle(String.format("%s %s %s",settings.getGameName(), settings.getTitleSeparator(), scene.getName()));
    }
    
    private AMenuScene classToInstance(Class scene){
        if(scene == null){
            return null;
        }
        if(!AMenuScene.class.isAssignableFrom(scene)){
            System.out.format("%s not assignable from %s", scene.getSimpleName(), AMenuScene.class.getSimpleName());
            return null;
        }
        try{
            AMenuScene sceneInstance = (AMenuScene)scene.getDeclaredMethod("getInstance").invoke(null);
            return sceneInstance;
        } catch (IllegalAccessException | NoSuchMethodException | 
                IllegalArgumentException | InvocationTargetException e){
            System.err.println("Couldn't get instance of new scene: " + e.getMessage());
        }
        return null;        
    }
    
    public NetWorks getReturnNetworks(){
        return this.returnNW;
    };
    
    public void gotoLobby(NetWorks nw){
        try{
            this.returnValue = ATunnelersStage.CHANGE_TO_GAME;
            this.returnNW = nw;
            this.close();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            this.changeScene(JoinScene.class);
        }
    }
}

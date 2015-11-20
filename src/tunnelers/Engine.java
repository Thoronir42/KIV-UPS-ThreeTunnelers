package tunnelers;

import tunnelers.Menu.MenuStage;
import java.util.Timer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tunnelers.Game.GameStage;
import tunnelers.Menu.MainMenuScene;

/**
 *
 * @author Stepan
 */
public class Engine extends Application {
    
    
    ATunnelersStage currentStage;
    Timer timer;
    Settings settings;
    
    @Override
    public void start(Stage primaryStage) {
        this.settings = Settings.getInstance();
        this.timer = new Timer();
        this.changeStage(MenuStage.getInstance());
        Assets.loadAssets();
        
        /*long delay = settings.getDelay();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                getStage().update();
            }
        }, delay, delay);*/
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private ATunnelersStage getStage(){
        return this.currentStage;
    }
    private void changeStage(ATunnelersStage stage){
        stage.setOnCloseRequest((WindowEvent event) -> {
            timer.cancel();
            stage.exit();
        });
        stage.setOnHidden((WindowEvent event) -> {
            stageHidden(stage.getReturnCode());
        });
        
        stage.setResizable(false);
        
        
        this.currentStage = stage;
        this.currentStage.show();
    }
    
    
    protected void stageHidden(int stageReturn){
        ATunnelersStage newStage;
        switch(stageReturn){
            case ATunnelersStage.CLOSE:
                Runtime.getRuntime().exit(0);
                break;
            case ATunnelersStage.CHANGE_TO_GAME:
                MenuStage oldStage = (MenuStage)this.getStage();
                 newStage = new GameStage(oldStage.getReturnNetworks());
                this.changeStage(newStage);                
                break;
            case ATunnelersStage.CHANGE_TO_MENU:
                newStage = MenuStage.getInstance();
                newStage.changeScene(MainMenuScene.class);
                this.changeStage(newStage);
                break;
                
        }
    }
}

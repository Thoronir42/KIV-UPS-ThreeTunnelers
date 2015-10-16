package tunnelers.Game;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import tunnelers.ATunnelersScene;

/**
 *
 * @author Stepan
 */
public abstract class AGameScene extends ATunnelersScene{

    public AGameScene(Parent root, double width, double height, String name) {
        super(root, width, height);
        this.name = name;
    }

    @Override
    public void handleKeyPressed(KeyCode code) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    protected GameStage getStage(){
        GameStage stage = (GameStage)this.getWindow();
        return stage;
    }
    
    protected void sendMessage(String message){
        this.getStage().getNetworks().sendMessage(message);
    }
    
    abstract void handleNetworkCommand(String command);
    
    abstract void updateChatbox();
    
}

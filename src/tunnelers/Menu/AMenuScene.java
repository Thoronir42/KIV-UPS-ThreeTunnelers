package tunnelers.Menu;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tunnelers.ATunnelersScene;

/**
 *
 * @author Stepan
 */
public abstract class AMenuScene extends ATunnelersScene{
    
    public AMenuScene(Parent root, double width, double height, String name) {
        super(root, width, height, name);
        
        this.setOnKeyPressed((KeyEvent event) -> {
            handleKeyPressed(event.getCode());
        });
    }
    
    @Override
    public final void handleKeyPressed(KeyCode code) {
        if(code.equals(KeyCode.ESCAPE)){
            this.getStage().prevScene();
        }
    }
    
    protected MenuStage getStage(){
        return (MenuStage)this.getRoot().getScene().getWindow();
    }
    
    public abstract Class getPrevScene();
    
}

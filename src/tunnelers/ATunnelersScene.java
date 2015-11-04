package tunnelers;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Stepan
 */
public abstract class ATunnelersScene extends Scene{
    
    protected static Settings settings = Settings.getInstance();
    
    protected String name;
    private static int sceneCount = 0;
    
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public ATunnelersScene(Parent root, double width, double height) {
        super(root, width, height);
        this.name = "scene "+(++sceneCount);
    }
    
    public ATunnelersScene(Parent root, double width, double height, String name){
        super(root, width, height);
        this.name = name;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunnelers.Menu;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Stepan
 */
public class SettingsScene extends AMenuScene{
    
    static SettingsScene instance;
    
    public static SettingsScene getInstance(){
        if(instance == null){
            instance = createInstance();
        }
        return instance;
    }

    private static SettingsScene createInstance(){
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #cedace");
        Button btn = new Button("Back to Main Menu");
        
        
        root.getChildren().add(btn);
        
        SettingsScene scene = new SettingsScene(root, settings.getWidth(), settings.getHeight());
        btn.setOnAction((ActionEvent event) -> {
            scene.getStage().changeScene(MainMenuScene.class);
        });
        return scene;
    }
    
    public SettingsScene(Parent root, double width, double height) {
        super(root, width, height, "Settings");
    }

    @Override
    public Class getPrevScene() {
        return MainMenuScene.class;
    }
    
    
}

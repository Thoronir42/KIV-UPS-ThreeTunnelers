package tunnelers.Game;

import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class PlayScene extends AGameScene{

    private static PlayScene instance;
    private NetWorks nw;
    
    
    
    public static PlayScene getInstance(NetWorks nw) throws IllegalStateException{
        if(instance == null){
            instance = createInstance();
        }        
        if(nw != null){
            instance.nw = nw;
        } else if (instance.nw == null){
            throw new IllegalStateException("Lobby didn't receive NetWorks container and none was previously set.");
        }
        return instance;
    }
    
    private static PlayScene createInstance(){
        BorderPane root = new BorderPane();
        
        root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.DIMGRAY.hashCode()));
        PlayScene scene = new PlayScene(root, settings.getWidth(), settings.getHeight());
        
        addComponents(root, scene);
        
        return scene;
        
    }

    protected TextArea ta_chatBox;
    protected TextField tf_chatIn;
    protected Canvas ca_drawArea;
    
    
    public PlayScene(Parent root, double width, double height) {
        super(root, width, height, "Battlefield");
    }
    
    private static void addComponents(BorderPane root, PlayScene scene){
        scene.ca_drawArea = new Canvas();
        root.setCenter(scene.ca_drawArea);
        
        VBox vertical = new VBox();
        
        scene.ta_chatBox = new TextArea();
        scene.ta_chatBox.setWrapText(true);
        scene.ta_chatBox.setPrefColumnCount(40);
        scene.ta_chatBox.setPrefRowCount(10);
        scene.ta_chatBox.setDisable(true);
        vertical.getChildren().add(scene.ta_chatBox);
        
        scene.tf_chatIn = new TextField();
        vertical.getChildren().add(scene.tf_chatIn);
        
        root.setRight(vertical);
        root.setOnMouseClicked((MouseEvent e) -> {
            scene.drawScene();
        });
    }
    
    @Override
    public void updateChatbox() {
        GameStage stage = this.getStage();
        this.ta_chatBox.setText(stage.getGamechat().getLog());
    }
    
    public void drawScene(){
        GraphicsContext g = this.ca_drawArea.getGraphicsContext2D();
        g.setStroke(Color.CRIMSON);
        g.strokeLine(0, 0, ca_drawArea.getWidth(), ca_drawArea.getHeight());
    }
}

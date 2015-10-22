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
import tunnelers.Game.CanvasLayouts.CanLayout_2x2;
import tunnelers.Game.CanvasLayouts.CanvasLayout;
import tunnelers.Game.CanvasLayouts.CanvasLayoutException;
import tunnelers.Game.structure.Container;
import tunnelers.Game.structure.Player;
import tunnelers.Game.structure.TunnelMap;

/**
 *
 * @author Stepan
 */
public class PlayScene extends AGameScene{
    
    public static PlayScene getInstance(){
        return createInstance();
    }
    
    private static Container mockContainer(){
        Player[] players = new Player[]{
            new Player("Yahoo"),
            new Player("Yello"),
            new Player("Yoda"),
            new Player("Ludo")
        };
        TunnelMap map = TunnelMap.getMockMap();
        Container c = new Container(players, map);
        
        return c;
    }
    
    private static PlayScene createInstance(){
        return createInstance(mockContainer());
    }
    
    private static PlayScene createInstance(Container c){
        BorderPane root = new BorderPane();
        
        root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.DIMGRAY.hashCode()));
        PlayScene scene = new PlayScene(root, settings.getWidth(), settings.getHeight(), c);
        
        addComponents(root, scene);
        
        return scene;
        
    }

    protected TextArea ta_chatBox;
    protected TextField tf_chatIn;
    protected Canvas ca_drawArea;
    protected CanvasLayout canvasLayout;
    
    
    public PlayScene(Parent root, double width, double height, Container container) {
        super(root, width, height, "Battlefield");
        this.canvasLayout = CanvasLayout.choseIdeal(container);
    }
    
    private static void addComponents(BorderPane root, PlayScene scene){
        int chatWidth = 160;
        
        scene.ca_drawArea = new Canvas(settings.getWidth() - chatWidth, settings.getHeight());
        root.setCenter(scene.ca_drawArea);
        
        VBox vertical = new VBox();
        
        scene.ta_chatBox = new TextArea();
        scene.ta_chatBox.setWrapText(true);
        scene.ta_chatBox.setPrefWidth(chatWidth);
        scene.ta_chatBox.setPrefRowCount(10);
        scene.ta_chatBox.setDisable(true);
        vertical.getChildren().add(scene.ta_chatBox);
        
        scene.tf_chatIn = new TextField();
        scene.tf_chatIn.setPrefWidth(chatWidth);
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
        this.canvasLayout.drawLayout(g);
    }
}

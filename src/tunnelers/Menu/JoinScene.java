package tunnelers.Menu;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class JoinScene extends AMenuScene{

    private static JoinScene instance;
    
    public static JoinScene getInstance(){
        if(instance == null){
            instance = createInstance();
        }
        return instance;
    }
    
    private static JoinScene createInstance(){
        GridPane root = new GridPane();
        root.setHgap(4);
        root.setVgap(18);
        
        root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.BLUEVIOLET.hashCode()));
        JoinScene scene = new JoinScene(root, settings.getWidth(), settings.getHeight());
        root.setAlignment(Pos.CENTER);
        addComponents(root, scene);
        
        return scene;
        
    }

    protected TextField tf_adress,
                        tf_port,
                        tf_clientName;
    
    public JoinScene(Parent root, double width, double height) {
        super(root, width, height, "Join Game");
    }
    
    @Override
    public Class getPrevScene() {
        return MainMenuScene.class;
    }
    
    private static void addComponents(GridPane root, JoinScene scene){
        Node next = new Label("Adress:");
        root.add(next, 0, 0);
        
        next = scene.tf_adress = new TextField("localhost");
        root.add(next, 1, 0);
        
        next = new Label("Port:");
        root.add(next, 0, 1);
        
        next = scene.tf_port = new TextField(String.valueOf(settings.getDefaultPort()));
        root.add(next, 1, 1);
        
        next = new Label("Client name:");
        root.add(next, 0, 2);
        
        next = scene.tf_clientName = new TextField("Faggot");
        root.add(next, 1, 2);
        
        
        next = new Button("Connect!");
        ((Button)next).setOnAction((ActionEvent event) -> {
            scene.connect();
        });
        root.add(next, 0, 3);
    }
    
    private void connect(){
        String address = tf_adress.getText(),
               client = tf_clientName.getText();
        int port = Integer.parseInt(tf_port.getText());
        System.out.format("Connecting to: %s:%d%n", address, port);
        try{
            NetWorks nw = NetWorks.connectTo(address, port, client);
            getStage().gotoLobby(nw);
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
    
}

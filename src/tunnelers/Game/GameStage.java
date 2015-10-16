package tunnelers.Game;


import java.util.logging.Level;
import java.util.logging.Logger;
import tunnelers.ATunnelersStage;
import tunnelers.network.MessagePasser;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public class GameStage extends ATunnelersStage{

    protected NetWorks networks;
    protected GameChat chatbox;
    
    public GameStage(NetWorks networks) {
        this.networks = networks;
        this.networks.setHandleMessage(new MessagePasser(){
            @Override
            public void run(){
                System.out.println("Stage received message "+this.getMessage());
                handleNetworkCommand(this.getMessage());
            }
        });
        this.setScene(LobbyScene.getInstance(networks));
        this.chatbox = new GameChat();
        this.networks.start();
    }

    @Override
    public void update() {
        
    }

    @Override
    public void exit() {
        try {
            this.networks.endCommunication();
            this.networks.join();
            System.out.println("NetWorks ended succesfully");
            super.exit();
        } catch (InterruptedException ex) {
            Logger.getLogger(GameStage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    @Override
    public void changeScene(Class scene) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void gotoMenu(){
        this.returnValue = CHANGE_TO_MENU;
        this.close();
    }
    
    protected NetWorks getNetworks(){
        return this.networks;
    }
    
    public void handleNetworkCommand(String command){
        AGameScene scene = (AGameScene)this.getScene();
        scene.handleNetworkCommand(command);
    }
    
}

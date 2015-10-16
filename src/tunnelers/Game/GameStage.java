package tunnelers.Game;


import java.util.logging.Level;
import java.util.logging.Logger;
import tunnelers.ATunnelersStage;
import tunnelers.network.MessagePasser;
import tunnelers.network.NetWorks;
import tunnelers.structure.Player;

/**
 *
 * @author Stepan
 */
public class GameStage extends ATunnelersStage{

    protected NetWorks networks;
    protected GameChat gamechat;
    
    public GameStage(NetWorks networks) {
        this.networks = networks;
        this.networks.setHandleMessage(new MessagePasser(){
            @Override
            public void run(){
                handleNetworkCommand(this.getMessage());
            }
        });
        this.setScene(LobbyScene.getInstance(networks));
        this.gamechat = new GameChat();
        this.networks.start();
    }

    @Override
    public void update() {
        
    }

    @Override
    public void exit() {
        try {
            this.networks.endCommunication();
            this.networks.interrupt();
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
    protected GameChat getGamechat(){
        return this.gamechat;
    }
    
    public void handleNetworkCommand(String command){
        AGameScene scene = (AGameScene)this.getScene();
        switch(0){
            default:
                System.err.println("Incomming command not recognised");
                break;
            case 0:
                String[] segs = command.split(":");
                this.gamechat.addMessage(new Player(segs[0]), segs[1]);
                ((AGameScene)this.getScene()).updateChatbox();
                break;
        }
        
        scene.handleNetworkCommand(command);
    }
    
}

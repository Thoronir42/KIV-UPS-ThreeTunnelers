package tunnelers.Game;


import javafx.geometry.Point2D;
import tunnelers.ATunnelersScene;
import tunnelers.ATunnelersStage;
import tunnelers.Game.structure.Container;
import tunnelers.Game.structure.Direction;
import tunnelers.network.NetCommandPasser;
import tunnelers.network.NetWorks;
import tunnelers.Game.structure.Player;
import tunnelers.network.NetCommand;

/**
 *
 * @author Stepan
 */
public class GameStage extends ATunnelersStage{

    protected NetWorks networks;
    protected GameChat gamechat;
    private Container container;
    private AGameScene sc;
    
    public GameStage(NetWorks networks) {
        this.networks = networks;
        this.networks.setCommandPasser(new NetCommandPasser(){
            @Override
            public void run(){
                handleNetworkCommand(this.getMessage());
            }
        });
        this.setScene(LobbyScene.getInstance(networks));
		this.container = Container.mockContainer();
        this.gamechat = new GameChat();
    }

    @Override
    public void update() {
        
    }

    @Override
    public void exit() {
        try {
            this.networks.disconnect();
            this.networks.interrupt();
            this.networks.join();
            System.out.println("NetWorks ended succesfully");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        super.exit(CHANGE_TO_MENU);
    }
    
    protected NetWorks getNetworks(){
        return this.networks;
    }
    protected GameChat getGamechat(){
        return this.gamechat;
    }
    
    protected Container getContainer(){
        return this.container;
    }
    
    protected void movePlayer(int pid, Direction d){
        Player[] players = this.container.getPlayers();
        Player plr = players[0];
        Point2D plr_loc = plr.getLocation();
        double newX = plr_loc.getX(), newY = plr_loc.getY();
        switch(d){
            default: return;
            case North:
                if(newY > 0){ newY -= 1; }
            break;
                
            case West:
                if(newX > 0){ newX -= 1; }
            break;
                
            case East:
                if(newX < this.container.getMapWidth()){ newX += 1; }
            break;
                
            case South:
                if(newY < this.container.getMapHeight()){ newY += 1; }
            break;
        }
        plr.setLocation(new Point2D(newX, newY));
        sc.drawScene();
    }
    
    public void handleNetworkCommand(NetCommand command){
        AGameScene scene = (AGameScene)this.getScene();
        switch(command.toString()){
            default:
                System.err.println("Incomming command not recognised");
                break;
            case "LEL":
                String[] segs = command.getCommandCode().split(NetCommand.COMMAND_SPLIT);
                this.gamechat.addMessage(new Player(segs[0]), segs[1]);
                scene.updateChatbox();
                break;
        }
    }
    
	protected void beginGame(){
		AGameScene sc = PlayScene.getInstance(container);
		this.changeScene(sc);
	}
	
    @Override
    protected void changeScene(ATunnelersScene scene) {
        super.changeScene(sc = (AGameScene)scene);
        sc.updateChatbox();
        sc.drawScene();
        
    }
    
    
    
}

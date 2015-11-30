package tunnelers.Game;


import javafx.geometry.Point2D;
import tunnelers.ATunnelersScene;
import tunnelers.ATunnelersStage;
import tunnelers.Game.structure.Container;
import tunnelers.Game.structure.Direction;
import generic.BackPasser;
import tunnelers.network.NetWorks;
import tunnelers.Game.structure.Player;
import tunnelers.Game.structure.Tank;
import tunnelers.network.MessageCommand;
import tunnelers.network.NCG.NetCommand;

/**
 *
 * @author Stepan
 */
public class GameStage extends ATunnelersStage{

    protected NetWorks networks;
    protected GameChat gamechat;
    private final Container container;
    private AGameScene sc;
	
    public GameStage(NetWorks networks) {
        this.networks = networks;
        this.networks.setCommandPasser(new BackPasser<NetCommand>(){
            @Override
            public void run(){
                handleNetworkCommand(this.get());
            }
        });
        this.setScene(LobbyScene.getInstance(networks));
		this.container = Container.mockContainer();
        this.gamechat = new GameChat();
    }

    @Override
    public final void update() {
		
    }

    @Override
    public void exit() {
        try {
            this.networks.disconnect();
            this.networks.interrupt();
            this.networks.join();
            System.out.println("NetWorks ended succesfully");
        } catch (InterruptedException ex) {
            System.err.println(ex.getClass().getSimpleName()+": "+ex.getMessage());
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
        Tank tank = this.container.getPlayer(pid).getTank();
        Point2D plr_loc = tank.getLocation();
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
        tank.setLocation(new Point2D(newX, newY));
		tank.setDirection(d);
        sc.drawScene();
    }
    
    public void handleNetworkCommand(NetCommand command){
        AGameScene scene = (AGameScene)this.getScene();
        if(command instanceof MessageCommand.Plain){
			MessageCommand.Plain cmd = (MessageCommand.Plain)command;
			String msg = cmd.getMessageText();
			Player p = this.container.getPlayer(cmd.getPlayerId());
			this.gamechat.addMessage(p, msg);
			scene.updateChatbox();
		} else {
			System.err.println("Incomming command not recognised");
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

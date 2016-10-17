package tunnelers.core.engine;

import tunnelers.core.model.player.APlayer;
import tunnelers.core.chat.Chat;
import tunnelers.core.io.InputAction;
import tunnelers.core.GameContainer;
import tunnelers.core.Warzone;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Stepan
 */
public final class Engine {

	private final int version;
	
	private GameContainer container;
	private final NetWorks networks;
	private final Chat chat;

	private AEngineStage currentStage;

	public Engine(int version, NetWorks networks, Chat chat) {
		this.version = version;
		this.networks = networks;
		this.chat = chat;
		
		this.setStage(EngineStage.Menu);
	}
	
	public void setContainer(GameContainer container){
		this.container = container;
	}
	
	public void setStage(EngineStage stage){
		switch(stage){
			case Menu:
				this.currentStage = new MenuStage();
				break;
			case Warzone:
				this.currentStage = new WarzoneStage(container);
				break;
		}
	}

	public GameContainer getContainer() {
		return container;
	}

	public void update(long tick) {
		this.currentStage.update(tick);
	}

	protected void handleNetworkCommand(Command command) {
		switch(command.getType()){
			case MsgPlain:
				String msg = command.toString();
				APlayer p = this.container.getPlayer(0);
				this.chat.addMessage(p, msg);
				break;
			default:
				System.err.println("Incomming command not recognised");
				break;
		}
	}

	public void exit() {
		this.networks.close();
	}

	public void handleInput(InputAction inp, int playerID, boolean pressed) {
		APlayer p = this.container.getPlayer(playerID);

		if (p.getControls().setControlState(inp, pressed)) {
			Command cmd = this.networks.createCommand(CommandType.GameControlsSet);
			
		}
	}

	public Chat getChat() {
		return chat;
	}

	public Warzone getWarzone() {
		return this.container.getWarzone();
	}
}

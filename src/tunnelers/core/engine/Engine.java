package tunnelers.core.engine;

import tunnelers.core.model.player.APlayer;
import java.util.List;
import tunnelers.core.chat.Chat;
import tunnelers.Game.IO.InputAction;
import tunnelers.core.GameContainer;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.network.NetWorks;
import tunnelers.network.command.GameCommand;
import tunnelers.network.command.MessageCommand;
import tunnelers.network.command.NCG;

/**
 *
 * @author Stepan
 */
public final class Engine {

	private final GameContainer container;
	private final NetWorks networks;
	private final Chat chat;

	private AEngineStage currentStage;

	public Engine(GameContainer container, NetWorks networks, Chat chat) {
		this.container = container;
		this.networks = networks;
		this.chat = chat;
		
		this.setStage(EngineStage.Menu);
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

	public APlayer getPlayer(int n) {
		return this.container.getPlayer(n);
	}

	public void update(long tick) {
		this.currentStage.update(tick);
	}

	public void handleNetworkCommand(NCG.NetCommand command) {
		if (command instanceof MessageCommand.Plain) {
			MessageCommand.Plain cmd = (MessageCommand.Plain) command;
			String msg = cmd.getMessageText();
			APlayer p = this.getPlayer(cmd.getPlayerId());
			this.chat.addMessage(p, msg);
		} else {
			System.err.println("Incomming command not recognised");
		}
	}

	public void exit() {
		this.networks.close();
	}

	public void handleInput(InputAction inp, int playerID, boolean pressed) {
		APlayer p = this.getPlayer(playerID);

		if (p.getControls().handleControl(inp, pressed)) {
			NCG.NetCommand cmd = new GameCommand.ControlSet(inp.intVal(), pressed ? 1 : 0);
		}
	}

	public List<APlayer> getPlayers() {
		return this.container.getPlayers();
	}
}

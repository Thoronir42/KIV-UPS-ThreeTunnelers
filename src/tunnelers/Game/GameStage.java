package tunnelers.Game;

import tunnelers.Game.Chat.Chat;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.TunnelersStage;
import tunnelers.model.GameContainer;
import generic.BackPasser;
import javafx.scene.input.KeyCode;
import tunnelers.Game.IO.InputAction;
import tunnelers.Game.IO.ControlInput;
import tunnelers.network.NetWorks;
import tunnelers.model.player.APlayer;
import tunnelers.model.Engine;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.network.command.GameCommand;
import tunnelers.network.command.MessageCommand;
import tunnelers.network.command.NCG;
import tunnelers.network.command.NCG.NetCommand;

/**
 *
 * @author Stepan
 */
public class GameStage extends TunnelersStage {

	private final ControlSchemeManager controlSchemeManager;

	public GameStage() {
		this.controlSchemeManager = SETTINGS.getControlSchemeManager();
		GameContainer container = GameContainer.mockContainer(this.controlSchemeManager, kickstarter.getLocalName());
		this.engine = new Engine(container);
		this.chat = new Chat(container.getLocalPlayer(), SETTINGS.getChatMessageCapacity());
	}

	protected GameContainer getContainer() {
		return this.engine.getContainer();
	}

	public void handleNetworkCommand(NetCommand command) {
		AGameScene scene = (AGameScene) this.getScene();
		if (command instanceof MessageCommand.Plain) {
			MessageCommand.Plain cmd = (MessageCommand.Plain) command;
			String msg = cmd.getMessageText();
			APlayer p = this.engine.getPlayer(cmd.getPlayerId());
			this.chat.addMessage(p, msg);
			scene.updateChatbox();
		} else {
			System.err.println("Incomming command not recognised");
		}
	}

	void handleKey(KeyCode code, boolean pressed) {
		ControlInput pi = this.controlSchemeManager.getPlayerInputByKeyPress(code);
		if(pi == null){
			return;
		}
		AControlScheme controlSchemeId = pi.getControlScheme();
		InputAction inp = pi.getInput();
		
		APlayer p = this.engine.getPlayer(controlSchemeId.getPlayerID());
		
		if (p.getControls().handleControl(inp, pressed)) {
			NCG.NetCommand cmd = new GameCommand.ControlSet(inp.intVal(), pressed ? 1 : 0);
		}
	}

	protected void beginGame() {
		ATunnelersScene sc = PlayScene.getInstance(engine.getContainer());
		this.changeScene(sc);
	}
}

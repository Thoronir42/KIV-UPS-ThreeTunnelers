package tunnelers.Game;

import tunnelers.Game.Chat.Chat;
import tunnelers.ATunnelersScene;
import tunnelers.ATunnelersStage;
import tunnelers.Game.Frame.Container;
import generic.BackPasser;
import javafx.scene.input.KeyCode;
import tunnelers.Game.IO.InputAction;
import tunnelers.Game.IO.ControlInput;
import tunnelers.network.NetWorks;
import tunnelers.Game.Frame.Player;
import tunnelers.Game.Frame.Engine;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.GameKickstarter;
import tunnelers.network.GameCommand;
import tunnelers.network.MessageCommand;
import tunnelers.network.NCG;
import tunnelers.network.NCG.NetCommand;

/**
 *
 * @author Stepan
 */
public class GameStage extends ATunnelersStage {

	protected NetWorks networks;
	protected Chat gamechat;
	protected final Engine engine;
	
	private AGameScene currentScene;
	private final ControlSchemeManager controlSchemeManager;

	public GameStage(GameKickstarter kickstarter) {
		this.networks = kickstarter.getNetworks();
		this.networks.setCommandPasser(new BackPasser<NetCommand>() {
			@Override
			public void run() {
				handleNetworkCommand(this.get());
			}
		});
		this.setScene(LobbyScene.getInstance(true)); // todo returning to same lobby from a game
		this.controlSchemeManager = SETTINGS.getControlSchemeManager();
		Container container = Container.mockContainer(this.controlSchemeManager, kickstarter.getLocalName());
		this.engine = new Engine(container);
		this.gamechat = new Chat(container.getLocalPlayer(), SETTINGS.getChatMessageCapacity());
	}

	@Override
	public final void update(long tick) {
		if (tick % 4 == 0 && currentScene instanceof PlayScene) {
			this.engine.update(tick);
			currentScene.drawScene();
		}
		
	}

	@Override
	public void exit() {
		try {
			this.networks.disconnect();
			this.networks.interrupt();
			this.networks.join();
			System.out.println("NetWorks ended succesfully");
		} catch (InterruptedException ex) {
			System.err.println("Failed to close networks: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
		}
		super.exit(CHANGE_TO_MENU);
	}

	protected NetWorks getNetworks() {
		return this.networks;
	}

	protected Chat getGamechat() {
		return this.gamechat;
	}

	protected Container getContainer() {
		return this.engine.getContainer();
	}

	public void handleNetworkCommand(NetCommand command) {
		AGameScene scene = (AGameScene) this.getScene();
		if (command instanceof MessageCommand.Plain) {
			MessageCommand.Plain cmd = (MessageCommand.Plain) command;
			String msg = cmd.getMessageText();
			Player p = this.engine.getPlayer(cmd.getPlayerId());
			this.gamechat.addMessage(p, msg);
			scene.updateChatbox();
		} else {
			System.err.println("Incomming command not recognised");
		}
	}

	void handleKey(KeyCode code, boolean pressed) {
		ControlInput pi= this.controlSchemeManager.getPlayerInputByKeyPress(code);
		if(pi == null){
			return;
		}
		AControlScheme controlSchemeId = pi.getControlScheme();
		InputAction inp = pi.getInput();
		
		Player p = this.engine.getPlayer(controlSchemeId.getPlayerID());
		
		if (p.getControls().handleControl(inp, pressed)) {
			NCG.NetCommand cmd = new GameCommand.ControlSet(inp.intVal(), pressed ? 1 : 0);
		}
	}

	protected void beginGame() {
		AGameScene sc = PlayScene.getInstance(engine.getContainer());
		this.changeScene(sc);
	}

	@Override
	protected void changeScene(ATunnelersScene scene) {
		super.changeScene(currentScene = (AGameScene) scene);
		currentScene.updateChatbox();
		currentScene.drawScene();

	}
}

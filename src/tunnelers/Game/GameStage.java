package tunnelers.Game;

import tunnelers.Game.Chat.Chat;
import tunnelers.ATunnelersScene;
import tunnelers.ATunnelersStage;
import tunnelers.Game.Frame.Container;
import generic.BackPasser;
import javafx.scene.input.KeyCode;
import tunnelers.Game.IO.Input;
import tunnelers.Game.IO.PlrInput;
import tunnelers.network.NetWorks;
import tunnelers.Game.Frame.Player;
import tunnelers.Game.Frame.Engine;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.network.ConnectionCommand;
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
	
	private AGameScene sc;
	private final ControlSchemeManager controlSchemeManager;

	public GameStage(NetWorks networks) {
		this.networks = networks;
		this.networks.setCommandPasser(new BackPasser<NetCommand>() {
			@Override
			public void run() {
				handleNetworkCommand(this.get());
			}
		});
		this.setScene(LobbyScene.getInstance(networks));
		this.gamechat = new Chat();
		this.controlSchemeManager = settings.getControlSchemeManager();
		this.engine = new Engine(Container.mockContainer(this.controlSchemeManager));
	}

	@Override
	public final void update(long tick) {
		if (tick % 4 == 0 && sc instanceof PlayScene) {
			this.engine.update(tick);
			sc.drawScene();
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
			System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
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
		PlrInput pi= this.controlSchemeManager.getPlayerInputByKeyPress(code);
		if(pi == null){
			return;
		}
		AControlScheme controlSchemeId = pi.getControlScheme();
		Input inp = pi.getInput();
		
		Player p = this.engine.getPlayer(controlSchemeId.getPlayerID());
		
		if (p.getControls().handleControl(inp, pressed)) {
			NCG.NetCommand cmd = new GameCommand.ControlSet(inp.intVal(), pressed ? 1 : 0);
		}
		//System.out.format("%s - %s%n", p.getName(), p.getControls());
	}

	protected void beginGame() {
		AGameScene sc = PlayScene.getInstance(engine.getContainer());
		this.changeScene(sc);
	}

	@Override
	protected void changeScene(ATunnelersScene scene) {
		super.changeScene(sc = (AGameScene) scene);
		sc.updateChatbox();
		sc.drawScene();

	}
}

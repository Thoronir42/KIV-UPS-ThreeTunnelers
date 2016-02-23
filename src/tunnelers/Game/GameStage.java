package tunnelers.Game;

import javafx.geometry.Point2D;
import tunnelers.ATunnelersScene;
import tunnelers.ATunnelersStage;
import tunnelers.Game.Frame.Container;
import tunnelers.Game.Frame.Direction;
import generic.BackPasser;
import javafx.scene.input.KeyCode;
import tunnelers.Game.IO.Input;
import tunnelers.Game.IO.KeyMap;
import tunnelers.Game.IO.PlrInput;
import tunnelers.network.NetWorks;
import tunnelers.Game.Frame.Player;
import tunnelers.Game.Frame.Tank;
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
	protected GameChat gamechat;
	private final Container container;
	private AGameScene sc;
	private final KeyMap keyMap;
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
		this.container = Container.mockContainer();
		this.gamechat = new GameChat();
		this.keyMap = settings.getKeyMap();
		this.controlSchemeManager = new ControlSchemeManager();
	}

	@Override
	public final void update(long tick) {
		if (tick % 4 == 0 && sc instanceof PlayScene) {
			updatePlayers();
			sc.drawScene();
		}

	}

	private void updatePlayers() {
		for (Player p : this.container.getPlayers()) {
			updatePlayer(p);
		}
	}

	protected void updatePlayer(Player p) {
		Tank tank = p.getTank();
		Direction d = p.getControls().getDirection();

		if (d == null) {
			return;
		}
		Point2D plr_loc = tank.getLocation();
		double newX = plr_loc.getX() + d.getX(),
				newY = plr_loc.getY() + d.getY();

		if ((newY - tank.getHeight() / 2 > 0)
				&& (newX - tank.getWidth() / 2 > 0)
				&& (newX + tank.getWidth() / 2 < this.container.getMap().getWidth())
				&& (newY + tank.getHeight() / 2 < this.container.getMap().getHeight())) {
			tank.setLocation(new Point2D(newX, newY));
			tank.setDirection(d);
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

	protected GameChat getGamechat() {
		return this.gamechat;
	}

	protected Container getContainer() {
		return this.container;
	}

	public void handleNetworkCommand(NetCommand command) {
		AGameScene scene = (AGameScene) this.getScene();
		if (command instanceof MessageCommand.Plain) {
			MessageCommand.Plain cmd = (MessageCommand.Plain) command;
			String msg = cmd.getMessageText();
			Player p = this.container.getPlayer(cmd.getPlayerId());
			this.gamechat.addMessage(p, msg);
			scene.updateChatbox();
		} else {
			System.err.println("Incomming command not recognised");
		}
	}

	void handleKey(KeyCode code, boolean pressed) {
		PlrInput pi = this.keyMap.getInput(code);
		if (pi == null) {
			return;
		}
		byte controlSchemeId = pi.getControlSchemeId();
		Input inp = pi.getInput();

		byte pIndex = this.controlSchemeManager.getPlayerIdFromScheme(controlSchemeId);
		
		Player p = this.container.getPlayer(pIndex);
		if (p.getControls().handleControl(inp, pressed)) {
			NCG.NetCommand cmd = new GameCommand.ControlSet(inp.intVal(), pressed ? 1 : 0);
		}
		//System.out.format("%s - %s%n", p.getName(), p.getControls());
	}

	protected void beginGame() {
		AGameScene sc = PlayScene.getInstance(container);
		this.changeScene(sc);
	}

	@Override
	protected void changeScene(ATunnelersScene scene) {
		super.changeScene(sc = (AGameScene) scene);
		sc.updateChatbox();
		sc.drawScene();

	}
}

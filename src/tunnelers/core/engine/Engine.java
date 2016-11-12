package tunnelers.core.engine;

import tunnelers.app.views.serverList.GameRoom;
import tunnelers.network.NetAdapter;
import tunnelers.core.player.Player;
import tunnelers.core.chat.Chat;
import tunnelers.core.player.InputAction;
import tunnelers.core.gameRoom.GameContainer;
import tunnelers.core.gameRoom.Warzone;
import tunnelers.core.chat.ServerMessenger;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.network.INetCommandHandler;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Stepan
 */
public final class Engine implements INetCommandHandler {

	private final int version;

	private GameContainer container;
	private final NetAdapter networks;
	private final Chat chat;

	private AEngineStage currentStage;

	private IView view;

	public Engine(int version, NetAdapter networks, Chat chat) {
		this.version = version;
		this.networks = networks;
		networks.setHandler(this);
		this.chat = chat;

		this.setStage(Stage.Menu);
	}

	public void setView(IView view) {
		this.view = view;
	}

	public void setContainer(GameContainer container) {
		this.container = container;
	}

	public void setStage(Stage stage) {
		switch (stage) {
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

	public void exit() {
		this.networks.shutdown();
	}

	public void handleInput(InputAction inp, int playerID, boolean pressed) {
		Player p = this.container.getPlayer(playerID);

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

	public void connect(String name, String addr, int port) {
		this.networks.connectTo(name, addr, port);
	}

	public void disconnect() {
		this.networks.disconnect("Disconnecting");
	}

	public void sendPlainText(String text) {
		Command cmd = this.networks.createCommand(CommandType.MsgPlain);
		cmd.setData(text);
		this.networks.issueCommand(cmd);
	}

	@Override
	public boolean handle(Command cmd) {
		System.out.println("Engine processing command: " + cmd.toString());
		switch (cmd.getType()) {
			case LeadApprove:
				System.out.println("Ano");
				return true;
			case MsgRcon:
			case MsgPlain:
				chat.addMessage(ServerMessenger.getInstance(), cmd.getData());
				view.updateChat();
				return true;
			case VirtConnectionEstabilished:
				view.showScene(IView.Scene.Lobby);
			case VirtConnectingError:
			case VirtConnectingTimedOut:
				System.err.println("Nepripojeno: " + cmd.getData());
				return true;
			default:
				System.err.println("Command was not handled: " + cmd.toString());
				return false;
		}
	}

	public void beginGame() {
		this.setStage(Engine.Stage.Warzone);
		this.view.showScene(IView.Scene.Game);
	}

	public void viewServerList() {
		// fixme: perform real server listing
		this.view.showScene(IView.Scene.ServerList);
	}

	public void joinGame(GameRoom gameRoom) {
		if (gameRoom.Full.get()) {
			this.view.alert("Hra je již plná");
			return;
		}
		this.view.alert("Probíhá připojování");

		this.view.showScene(IView.Scene.Lobby);
	}

	public static enum Stage {
		Menu,
		Warzone,
	}
}

package tunnelers.core.engine;

import tunnelers.core.view.IView;
import temp.mapGenerator.MapGenerator;
import temp.Mock;
import tunnelers.common.IUpdatable;
import tunnelers.app.views.serverList.GameRoom;
import tunnelers.network.NetAdapter;
import tunnelers.core.player.Player;
import tunnelers.core.chat.Chat;
import tunnelers.core.player.controls.InputAction;
import tunnelers.core.gameRoom.GameContainer;
import tunnelers.core.gameRoom.Warzone;
import tunnelers.core.chat.ServerMessenger;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.settings.Settings;
import tunnelers.network.INetCommandHandler;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Stepan
 */
public final class Engine implements INetCommandHandler, IUpdatable {

	private final int version;

	private GameContainer container;
	private final NetAdapter netadapter;
	private final Chat chat;

	private AEngineStage currentStage;

	private IView view;
	private final AControlsManager controls;
	private final int tickRate;
	
	private final PersistentString connectionSecret;

	public Engine(int version, AControlsManager controls, Settings settings) {
		this.version = version;
		this.netadapter = new NetAdapter(this);
		this.controls = controls;

		this.chat = new Chat(settings.getChatMessageCapacity());

		this.setStage(Stage.Menu);
		this.tickRate = settings.getTickRate();
		this.connectionSecret = new PersistentString(settings.getConnectionLogRelativePath());
	}

	public void start() {
		this.netadapter.start();
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

	@Override
	public void update(long tick) {
		this.currentStage.update(tick);
		if (tick % (tickRate / 2) == 0) {
			netadapter.update(tick);
		}
	}

	public void exit() {
		this.netadapter.shutdown();
	}

	public void handleInput(InputAction inp, int playerID, boolean pressed) {
		Player p = this.container.getPlayer(playerID);

		if (p.getControls().setControlState(inp, pressed)) {
			Command cmd = this.netadapter.createCommand(CommandType.GameControlsSet);
		}
	}

	public Chat getChat() {
		return chat;
	}

	public Warzone getWarzone() {
		return this.container.getWarzone();
	}

	public void connect(String name, String addr, int port) {
		this.netadapter.connectTo(name, addr, port);
	}

	public void disconnect() {
		this.netadapter.disconnect("Disconnecting");
	}

	public void sendPlainText(String text) {
		Command cmd = this.netadapter.createCommand(CommandType.MsgPlain);
		cmd.setData(text);
		this.netadapter.issueCommand(cmd);
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
			case VirtConnectionTerminated:
				view.showScene(IView.Scene.MainMenu);
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

	public void refreshServerList() {
		// TODO: implement
	}

	public void joinGame(GameRoom gameRoom) {
		// TODO: link this through network events
		container = Mock.gameContainer(controls, view.getColorScheme().getAvailablePlayerColors());
		container.initWarzone((new MapGenerator()).mockMap(20, 12, 8, container.getPlayerCount()));
		
		this.view.prepareGame(container.getWarzone().getMap(), container.getPlayers());
		
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

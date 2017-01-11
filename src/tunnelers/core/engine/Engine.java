package tunnelers.core.engine;

import tunnelers.core.view.IView;
import temp.mapGenerator.MapGenerator;
import temp.Mock;
import tunnelers.common.IUpdatable;
import tunnelers.network.NetAdapter;
import tunnelers.core.player.Player;
import tunnelers.core.chat.Chat;
import tunnelers.core.player.controls.InputAction;
import tunnelers.core.gameRoom.Warzone;
import tunnelers.core.chat.ServerMessenger;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.IGameRoomInfo;
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

	private final int tickRate;
	private AEngineStage currentStage;
	
	private final NetAdapter netadapter;
	private final PersistentString connectionSecret;
	private final GameRoomParser gameRoomParser;
	
	private GameRoom currentGameRoom;
	
	private IView view;
	private AControlsManager controls;

	public Engine(int version, Settings settings) {
		this.version = version;
		this.netadapter = new NetAdapter(this);
		this.gameRoomParser = new GameRoomParser();

		this.setStage(Stage.Menu);
		this.tickRate = settings.getTickRate();
		this.connectionSecret = new PersistentString(settings.getConnectionLogRelativePath());
	}
	
	public void setView(IView view){
		this.view = view;
		this.controls = view.getControlsManager();
		
	}

	public void start() {
		this.netadapter.start();
	}

	public void setGameRoom(GameRoom gameRoom) {
		this.currentGameRoom = gameRoom;
	}

	public void setStage(Stage stage) {
		switch (stage) {
			case Menu:
				this.currentStage = new MenuStage();
				break;
			case Warzone:
				this.currentStage = new WarzoneStage(currentGameRoom);
				break;
		}
	}

	public GameRoom getGameRoom() {
		return currentGameRoom;
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
		Player p = this.currentGameRoom.getPlayer(playerID);

		if (p.getControls().setControlState(inp, pressed)) {
			Command cmd = this.netadapter.createCommand(CommandType.GameControlsSet);
		}
	}

	public Chat getChat() {
		return currentGameRoom.getChat();
	}

	public Warzone getWarzone() {
		return this.currentGameRoom.getWarzone();
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
		System.out.format("Engine processing command '%s': [%s]\n", cmd.getType().toString(), cmd.getData());
		switch (cmd.getType()) {
			case LeadApprove:
				System.out.println("Ano");
				return true;
			case MsgRcon:
			case MsgPlain:
				this.getChat().addMessage(ServerMessenger.getInstance(), cmd.getData());
				view.updateChat();
				return true;
			case VirtConnectionEstabilished:
				view.showScene(IView.Scene.ServerList);
			case VirtConnectingError:
			case VirtConnectingTimedOut:
				System.err.println("Nepripojeno: " + cmd.getData());
				return true;
			case VirtConnectionTerminated:
				view.alert("Spojení bylo ukončeno: " + cmd.getData());
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
		int n = 16;
		String lobbiesString = Mock.serverListString(16);
		IGameRoomInfo[] rooms = this.gameRoomParser.parse(16, lobbiesString.substring(4));
		this.view.appendGameRoomsToList(rooms);
	}

	public void joinGame(IGameRoomInfo gameRoom) {
		if (gameRoom.isFull()) {
			this.view.alert("Hra je již plná");
			return;
		}
		PlayerColorManager playerColorManager = view.getColorScheme().getPlayerColorManager();
		playerColorManager.resetColorUsage();

		// TODO: link this through network events
		this.currentGameRoom = Mock.gameRoom(controls, playerColorManager);
		this.currentGameRoom.initWarzone((new MapGenerator()).mockMap(20, 12, 8, this.currentGameRoom.getPlayerCount()));

		this.view.prepareGame(this.currentGameRoom.getWarzone().getMap(), this.currentGameRoom.getPlayers());

		
		this.view.alert("Probíhá připojování");

		this.view.showScene(IView.Scene.Lobby);
	}

	public static enum Stage {
		Menu,
		Warzone,
	}
}

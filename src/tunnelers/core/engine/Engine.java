package tunnelers.core.engine;

import generic.SimpleScanner;
import temp.mapGenerator.MapGenerator;
import tunnelers.common.IUpdatable;
import tunnelers.network.NetAdapter;
import tunnelers.core.chat.Chat;
import tunnelers.core.chat.IChatParticipant;
import tunnelers.core.gameRoom.Warzone;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.settings.Settings;
import tunnelers.network.CommandNotHandledException;
import tunnelers.network.NetClient;
import tunnelers.network.command.Command;
import tunnelers.network.command.INetworkProcessor;
import tunnelers.network.command.Signal;

/**
 *
 * @author Stepan
 */
public final class Engine implements INetworkProcessor, IUpdatable {

	private final int version;

	private final int tickRate;
	private AEngineStage currentStage;

	protected final EngineUserInterface guiInterface;

	protected final NetAdapter netadapter;
	protected final PersistentString connectionSecret;
	protected final GameRoomParser gameRoomParser;
	protected final SimpleScanner commandScanner;

	protected GameRoom currentGameRoom;

	protected IView view;
	protected AControlsManager controls;

	public Engine(int version, Settings settings) {
		this.version = version;
		this.netadapter = new NetAdapter(this);
		this.gameRoomParser = new GameRoomParser();

		this.setStage(Stage.Menu);
		this.tickRate = settings.getTickRate();
		this.connectionSecret = new PersistentString(settings.getConnectionLogRelativePath());

		this.guiInterface = new EngineUserInterface(this);
		this.commandScanner = new SimpleScanner(SimpleScanner.RADIX_HEXADECIMAL);
	}

	public EngineUserInterface intefrace() {
		return guiInterface;
	}

	public void setView(IView view) {
		this.view = view;
		this.controls = view.getControlsManager();

	}

	public void start() {
		this.netadapter.start();
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

	public Chat getChat() {
		if(this.currentGameRoom == null){
			return null;
		}
		return this.currentGameRoom.getChat();
	}

	public Warzone getWarzone() {
		if (this.currentGameRoom == null) {
			return null;
		}
		return this.currentGameRoom.getWarzone();
	}

	@Override
	public void handle(Command cmd) throws CommandNotHandledException {
		System.out.format("Engine processing command '%s': [%s]\n", cmd.getType().toString(), cmd.getData());
		commandScanner.setSourceString(cmd.getData());
		switch (cmd.getType()) {
			case LeadIntroduce:
				
			case MsgRcon:
				System.err.println("Rcon received, interpretting as plain msg");
				break;
			case MsgPlain:
				int id = commandScanner.nextByte();
				String message = commandScanner.readToEnd();
				if(id == 0){
					
				}
				NetClient client = currentGameRoom.getClient(id);
				IChatParticipant p = client.getAnyPlayer();
				this.getChat().addMessage(p != null ? p : Chat.error(), message);
				view.updateChat();
				break;
			default:
				throw new CommandNotHandledException(cmd);
		}
	}

	@Override
	public void signal(Signal signal) {
		view.setConnectEnabled(true);
		switch (signal.getType()) {
			case ConnectionEstabilished:
				view.showScene(IView.Scene.GameRoomList);
			case UnknownHost:
				System.err.println("Adresa nebyla rozpoznána: " + signal.getMessage());
				break;
			case ConnectingTimedOut:
				System.err.println("Čas pro navázání spojení vypršel: " + signal.getMessage());
				break;
			case ConnectionReset:
				view.alert("Spojení bylo ukončeno: " + signal.getMessage());
				view.showScene(IView.Scene.MainMenu);
				break;
		}
	}

	public void beginGame() {
		System.out.println("Generating map");
		Map map = (new MapGenerator()).mockMap(20, 12, 8, this.currentGameRoom.getPlayerCount());

		System.out.println("Initializing warzone");
		this.currentGameRoom.initWarzone(map);

		System.out.println("Preparing game");

		this.view.prepareGame(this.currentGameRoom.getWarzone().getMap(), this.currentGameRoom.getPlayers());

		this.setStage(Engine.Stage.Warzone);
		this.view.showScene(IView.Scene.Game);
	}

	public static enum Stage {
		Menu,
		Warzone,
	}
}

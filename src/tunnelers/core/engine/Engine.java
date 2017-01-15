package tunnelers.core.engine;

import generic.SimpleScanner;
import java.util.HashMap;
import temp.mapGenerator.MapGenerator;
import tunnelers.common.IUpdatable;
import tunnelers.network.NetAdapter;
import tunnelers.core.chat.Chat;
import tunnelers.core.chat.IChatParticipant;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.controls.AControlsManager;
import tunnelers.core.settings.Settings;
import tunnelers.network.NetClient;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandType;
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
	protected final HashMap<CommandType, IAction> commandActions;

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
		this.commandActions = this.prepareActions();

		String missing = "";
		int n = 0;
		for (CommandType type : CommandType.values()) {
			if (type == CommandType.Undefined) {
				continue;
			}
			if (!this.commandActions.containsKey(type)) {
				missing += type.toString() + ", ";
			}
			if (++n % 10 == 0) {
				missing += "\n";
			}
		}

		if (missing.length() > 0) {
			System.err.println("Engine does not implement handling of these "
					+ "command types: " + missing.substring(0, missing.length() - 2));
		}
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
		if (this.currentGameRoom == null) {
			return null;
		}
		return this.currentGameRoom.getChat();
	}

	@Override
	public boolean handle(Command cmd) {
		System.out.format("Engine processing command '%s': [%s]\n", cmd.getType().toString(), cmd.getData());
		commandScanner.setSourceString(cmd.getData());
		IAction action = this.commandActions.get(cmd.getType());
		
		if (action == null) {
			System.err.format("Action for %s not implemented\n", cmd.getType());
			return false;
		}
		return action.execute(commandScanner);
	}

	@Override
	public void signal(Signal signal) {
		view.setConnectEnabled(true);
		switch (signal.getType()) {
			case ConnectionEstabilished:
				view.showScene(IView.Scene.GameRoomList);
				break;
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

	private void prepareMap(int chunkSize, int xChunks, int yChunks) {
		Map map = new Map(chunkSize, xChunks, yChunks, currentGameRoom.getPlayerCount());
		// todo remove mocking
		map = (new MapGenerator()).generate(chunkSize, xChunks, yChunks, this.currentGameRoom.getPlayerCount());
		this.currentGameRoom.initWarzone(map);
	}

	public void beginGame() {
		this.prepareMap(20, 12, 8);

		this.view.setGameData(this.currentGameRoom.getMap(), this.currentGameRoom.getPlayers());

		this.setStage(Engine.Stage.Warzone);
		this.view.showScene(IView.Scene.Game);
	}

	public static enum Stage {
		Menu,
		Warzone,
	}

	private HashMap<CommandType, IAction> prepareActions() {
		HashMap<CommandType, IAction> map = new HashMap<>();
		this.putSoloCommands(map);
		this.putRoomCommands(map);

		return map;
	}

	private void putSoloCommands(HashMap<CommandType, IAction> map) {
		map.put(CommandType.LeadIntroduce, ((sc) -> {
			int n = sc.nextByte();
			String secret = sc.readToEnd();
			System.out.println(n + "=" + secret);
			this.connectionSecret.set(secret);

			return true;
		}));

		map.put(CommandType.LeadDisconnect, sc -> {
			this.netadapter.disconnect(sc.readToEnd());
			return true;
		});

		map.put(CommandType.LeadMarco, sc -> {
			Command polo = netadapter.createCommand(CommandType.LeadPolo);
			polo.append(sc.nextLong());
			return true;
		});

		map.put(CommandType.LeadBadFormat, sc -> {
			System.err.println("Server did not recognise folliwing command: " + sc.readToEnd());

			return true;
		});

		map.put(CommandType.MsgRcon, sc -> {
			System.err.println("Rcon messages not implemented");
			return false;
		});

	}

	private void putRoomCommands(HashMap<CommandType, IAction> map) {
		map.put(CommandType.MsgPlain, sc -> {
			int id = sc.nextByte();
			String message = sc.readToEnd();
			if (id == 0) {

			}
			NetClient client = currentGameRoom.getClient(id);
			IChatParticipant p = client.getAnyPlayer();
			this.getChat().addMessage(p != null ? p : Chat.error(), message);
			view.updateChat();
			return true;
		});

		map.put(CommandType.MapSpecification, sc -> {
			int chunkSize = sc.nextByte();
			int xChunks = sc.nextByte();
			int yChunks = sc.nextByte();
			prepareMap(chunkSize, xChunks, yChunks);

			return true;
		});
	}
}

package tunnelers.core.engine;

import generic.SimpleScanner;
import generic.SimpleScannerException;
import java.util.HashMap;
import tunnelers.common.IUpdatable;
import tunnelers.network.NetAdapter;
import tunnelers.core.chat.Chat;
import tunnelers.core.chat.IChatParticipant;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.engine.stage.AEngineStage;
import tunnelers.core.engine.stage.MenuStage;
import tunnelers.core.engine.stage.WarzoneStage;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.ChunkException;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;
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

	protected NetClient localClient;
	protected final NetAdapter netadapter;
	protected final PersistentString connectionSecret;

	protected final GameRoomParser gameRoomParser;
	protected final MapChunkParser mapChunkParser;

	protected final SimpleScanner commandScanner;
	protected final HashMap<CommandType, IAction> commandActions;

	protected GameRoom currentGameRoom;

	protected IView view;
	protected AControlsManager controls;
	protected String preferredName;

	public Engine(int version, Settings settings) {
		this.version = version;
		this.netadapter = new NetAdapter(this);
		this.gameRoomParser = new GameRoomParser();
		this.mapChunkParser = new MapChunkParser();

		this.setStage(Stage.Menu);
		this.tickRate = settings.getTickRate();
		this.connectionSecret = new PersistentString(settings.getConnectionLogRelativePath());

		this.guiInterface = new EngineUserInterface(this);
		this.commandScanner = new SimpleScanner(SimpleScanner.RADIX_HEXADECIMAL);
		this.commandActions = this.prepareActions();

		this.preferredName = "";

		String missing = "";
		int n = 0;
		for (CommandType type : CommandType.values()) {
			if (type == CommandType.Undefined) {
				continue;
			}
			if (!this.commandActions.containsKey(type)) {
				missing += type.toString() + ", ";
				if (++n % 8 == 0) {
					missing += "\n";
				}
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
			System.err.format("Engine: No action for %s\n", cmd.getType());
			return false;
		}
		try{
			return action.execute(commandScanner);
		} catch (SimpleScannerException ex){
			System.err.println(ex);
			return false;
		}
		
	}

	@Override
	public void signal(Signal signal) {
		System.out.println("Engine: processing signal " + signal.getType());
		view.setConnectEnabled(true);
		switch (signal.getType()) {
			case ConnectionEstabilished:
				this.localClient = new NetClient();
				view.showScene(IView.Scene.GameRoomList);
				break;
			case UnknownHost:
				view.alert("Adresa nebyla rozpoznána: " + signal.getMessage());
				break;
			case ConnectingTimedOut:
				view.alert("Čas pro navázání spojení vypršel");
				break;
			case ConnectionNoRouteToHost:
				view.alert("Připojení k internetu není k dispozici");
				break;
			case ConnectingFailedUnexpectedError:
				view.alert("Neznámá chyba navazování spojení: " + signal.getMessage());
				break;
			case ConnectionReset:
				this.localClient = null;
				view.alert("Spojení bylo ukončeno: " + signal.getMessage());
				view.showScene(IView.Scene.MainMenu);
				break;
		}
	}

	public static enum Stage {
		Menu,
		Warzone,
	}

	private HashMap<CommandType, IAction> prepareActions() {
		HashMap<CommandType, IAction> map = new HashMap<>();
		this.putSoloCommands(map);
		this.putRoomPreparationCommands(map);
		this.putRoomGameplayCommands(map);

		return map;
	}

	private void putSoloCommands(HashMap<CommandType, IAction> map) {
		map.put(CommandType.LeadIntroduce, ((sc) -> {
			int n = sc.nextByte();
			String secret = sc.readToEnd();
			this.connectionSecret.set(secret);

			Command setName = this.netadapter.createCommand(CommandType.ClientSetName)
					.append(this.preferredName);

			this.netadapter.send(setName);

			return true;
		}));

		map.put(CommandType.LeadDisconnect, sc -> {
			this.netadapter.disconnect(sc.readToEnd());
			return true;
		});

		map.put(CommandType.LeadMarco, sc -> {
			Command polo = netadapter.createCommand(CommandType.LeadPolo);
			int n = sc.nextByte();
			polo.append(sc.nextLong());
			this.netadapter.send(polo);

			return true;
		});

		map.put(CommandType.LeadPolo, sc -> {
			long now = System.currentTimeMillis();
			long stamp = sc.nextLong();
			long d = now - stamp;
			System.out.format("Polo: %d - %d = %d\n", now, stamp, d);

			return true;
		});

		map.put(CommandType.LeadBadFormat, sc -> {
			System.err.println("Server did not recognise folliwing command: " + sc.readToEnd());

			return true;
		});

		map.put(CommandType.ClientSetName, sc -> {
			this.localClient.setName(sc.readToEnd());
			return true;
		});

		map.put(CommandType.RoomsList, sc -> {
			this.view.appendGameRoomsToList(gameRoomParser.parse(sc.readToEnd()));

			return true;
		});

		map.put(CommandType.RoomsJoin, sc -> {
			int gameRoomId = sc.nextByte();
			int localClientRID = sc.nextByte();
			int leaderClientRID = sc.nextByte();

			this.currentGameRoom = new GameRoom(leaderClientRID, 4, 12, 4 * 20);
			this.currentGameRoom.setClient(localClientRID, this.localClient);

			PlayerColorManager playerColorManager = this.view.getPlayerColorManager();
			playerColorManager.resetColorUsage();

			this.view.showScene(IView.Scene.Lobby);

			return true;
		});

		map.put(CommandType.RoomsLeave, sc -> {
			this.view.showScene(IView.Scene.GameRoomList);
			this.view.alert(sc.readToEnd());
			this.currentGameRoom = null;

			return true;
		});

	}

	private void putRoomPreparationCommands(HashMap<CommandType, IAction> map) {
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

		map.put(CommandType.RoomSyncPhase, sc -> {
			int phaseNumber = sc.nextByte();
			System.out.println("Change room phase to " + phaseNumber);
			
			return false;
		});

		map.put(CommandType.RoomReadyState, sc -> {
			int readyState = sc.nextByte();
			int clientRID = sc.nextByte();

			NetClient c = this.currentGameRoom.getClient(clientRID);
			if (c == null) {
				return false;
			}
			c.setReady(readyState != 0);

			view.setLocalReadyState(readyState != 0);
			return true;
		});

		map.put(CommandType.RoomClientInfo, sc -> {
			int clientRID = sc.nextByte();
			String name = sc.readToEnd();

			NetClient c = this.currentGameRoom.getClient(clientRID);
			if (c == null) {
				c = new NetClient();
			}
			c.setName(name);
			this.currentGameRoom.setClient(clientRID, c);

			this.view.updateClients();
			return true;
		});

		map.put(CommandType.RoomClientLatency, sc -> {
			NetClient c = this.currentGameRoom.getClient(sc.nextByte());
			c.setLatency(sc.nextShort());

			this.view.updateClients();
			return true;
		});

		map.put(CommandType.RoomClientRemove, sc -> {
			int roomId = sc.nextByte();
			String reason = sc.readToEnd();

			NetClient c = this.currentGameRoom.getClient(roomId);

			if (c == localClient) {
				return (map.get(CommandType.RoomsLeave)).execute(sc);
			}

			this.currentGameRoom.removeClient(roomId);
			this.currentGameRoom.removePlayersOfClient(c);

			this.view.updateClients();
			this.view.updatePlayers();
			return true;
		});

		map.put(CommandType.RoomSetLeader, sc -> {
			int roomId = sc.nextByte();
			NetClient c = this.currentGameRoom.getClient(roomId);
			System.out.println("New room leader is " + c.getName());
			// todo actually change room leadership

			this.view.updateClients();
			return true;
		});

		map.put(CommandType.RoomPlayerAttach, sc -> {
			int playerRoomId = sc.nextByte();
			int playerColor = sc.nextByte();
			int clientRoomId = sc.nextByte();

			NetClient c = this.currentGameRoom.getClient(clientRoomId);
			Player p = new Player(c, playerColor);

			this.currentGameRoom.setPlayer(playerRoomId, p);

			this.view.updatePlayers();
			return true;
		});

		map.put(CommandType.RoomPlayerDetach, sc -> {
			int roomId = sc.nextByte();

			this.currentGameRoom.removePlayer(roomId);
			return true;
		});

		map.put(CommandType.RoomPlayerSetColor, sc -> {
			int roomId = sc.nextByte();
			int playerColor = sc.nextByte();

			this.currentGameRoom.getPlayer(roomId).setColor(playerColor);
			return true;
		});

	}

	private void putRoomGameplayCommands(HashMap<CommandType, IAction> map) {
		map.put(CommandType.MapSpecification, sc -> {
			int chunkSize = sc.nextByte();
			int xChunks = sc.nextByte();
			int yChunks = sc.nextByte();

			Map tunnelerMap = new Map(chunkSize, xChunks, yChunks, currentGameRoom.getPlayerCount());
			this.currentGameRoom.initWarzone(tunnelerMap);

			return true;
		});

		map.put(CommandType.MapChunkData, sc -> {
			int chunkX = sc.nextByte();
			int chunkY = sc.nextByte();
			int checkSum = sc.nextByte();
			Block[] chunkData = this.mapChunkParser.parseData(sc);
			try{
				if(!this.currentGameRoom.getWarzone().getMap()
					.updateChunk(chunkX, chunkY, chunkData)){
					System.err.format("Errors occured while updating chunk x=%d, y=%d\n", chunkX, chunkY);	
				}
				return true;
			} catch (ChunkException ex){
				System.err.println("Chunk data error: " + ex);
				return false;
			}
			
		});

		map.put(CommandType.MapBlocksChanges, sc -> {
			int n = sc.nextByte();
			Map tunnelerMap = this.currentGameRoom.getWarzone().getMap();
			for (int i = 0; i < n; i++) {
				int blockX = sc.nextShort();
				int blockY = sc.nextShort();
				Block newValue = Block.fromByteValue(Byte.parseByte(sc.read(1), 16));

				tunnelerMap.setBlock(blockX, blockY, newValue);
			}
			return true;
		});

		map.put(CommandType.GameControlsSet, sc -> {
			int roomId = sc.nextByte();
			int newState = sc.nextByte();

			Player p = this.currentGameRoom.getPlayer(roomId);
			p.getControls().setState(newState);

			return true;
		});

		map.put(CommandType.GameTankInfo, sc -> {
			int roomId = sc.nextByte();
			int x = sc.nextShort();
			int y = sc.nextShort();
			Direction direction = Direction.fromByteValue((byte) sc.nextByte());
			int hitPoints = sc.nextByte();
			int energy = sc.nextByte();

			Tank t = this.currentGameRoom.getWarzone().getTank(roomId);
			t.setLocation(x, y);
			t.setDirection(direction);
			t.setHitPoints(hitPoints);
			t.setEnergy(energy);

			return true;
		});

		map.put(CommandType.GameProjAdd, sc -> {
			int n = sc.nextByte();
			int playerRoomId = sc.nextByte();
			Direction direction = Direction.fromByteValue((byte) sc.nextByte());

			Player p = this.currentGameRoom.getPlayer(playerRoomId);
			Tank t = this.currentGameRoom.getWarzone().getTank(playerRoomId);

			this.currentGameRoom.getWarzone().setProjectile(n, t.getLocation(), direction, p);

			return true;
		});

		map.put(CommandType.GameProjRem, sc -> {
			int n = sc.nextByte();
			this.currentGameRoom.getWarzone().removeProjectile(n);
			return true;
		});
	}
}

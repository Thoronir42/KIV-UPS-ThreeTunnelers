package tunnelers.core.engine;

import tunnelers.core.chat.Chat;
import tunnelers.core.chat.IChatParticipant;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.GameRoomState;
import tunnelers.core.gameRoom.WarZone;
import tunnelers.core.gameRoom.WarZoneRules;
import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.ChunkException;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;
import tunnelers.network.NetClient;
import tunnelers.network.NetClientStatus;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandType;


public class EngineNetworksInterface {

	private final CommandMap actions;
	protected final Engine engine;

	private final GameRoomParser gameRoomParser;
	private final MapChunkParser mapChunkParser;

	private int remainingChunks;

	EngineNetworksInterface(Engine engine, boolean printUnimplementedActions) {
		this.engine = engine;
		this.actions = this.prepareActions();

		this.gameRoomParser = new GameRoomParser();
		this.mapChunkParser = new MapChunkParser();

		if (printUnimplementedActions) {
			this.actions.printUnimplementedActions(System.err);
		}
	}

	IAction getAction(CommandType type) {
		return this.actions.get(type);
	}

	private CommandMap prepareActions() {
		CommandMap map = new CommandMap();
		putSoloCommands(map);
		putRoomPreparationCommands(map);
		putRoomGamePlayCommands(map);

		return map;
	}

	private void putSoloCommands(CommandMap map) {
		map.put(CommandType.LeadIntroduce, ((sc) -> {
			int n = sc.nextByte();
			String secret = sc.readToEnd();
			this.engine.connectionSecret.set(secret);

			Command setName = this.engine.netadapter.createCommand(CommandType.ClientSetName)
					.append(this.engine.preferredName);

			this.engine.netadapter.send(setName);

			return true;
		}));

		map.put(CommandType.LeadDisconnect, sc -> {
			this.engine.netadapter.disconnect(sc.readToEnd());
			return true;
		});

		map.put(CommandType.LeadMarco, sc -> {
			Command polo = this.engine.netadapter.createCommand(CommandType.LeadPolo);
			int n = sc.nextByte();
			polo.append(sc.nextLong());
			this.engine.netadapter.send(polo);

			return true;
		});

		map.put(CommandType.LeadPolo, sc -> {
			long now = System.currentTimeMillis();
			long stamp = sc.nextLong();
			long d = now - stamp;
			System.out.format("Polo: %d - %d = %d\n", now, stamp, d);

			return true;
		});

		map.put(CommandType.ClientSetName, sc -> {
			this.engine.localClient.setName(sc.readToEnd());
			return true;
		});

		map.put(CommandType.RoomsList, sc -> {
			this.engine.view.appendGameRoomsToList(gameRoomParser.parse(sc.readToEnd()));

			return true;
		});

		map.put(CommandType.RoomsJoin, sc -> {
			int gameRoomId = sc.nextByte();
			int localClientRID = sc.nextByte();
			int leaderClientRID = sc.nextByte();

			this.engine.currentGameRoom = new GameRoom(leaderClientRID, 4, 12);
			this.engine.currentGameRoom.setClient(localClientRID, this.engine.localClient);

			PlayerColorManager playerColorManager = this.engine.view.getPlayerColorManager();
			playerColorManager.resetColorUsage();

			this.engine.view.showScene(IView.Scene.Lobby);

			return true;
		});

		map.put(CommandType.RoomsLeave, sc -> {
			this.engine.view.showScene(IView.Scene.GameRoomList);
			this.engine.view.alert(sc.readToEnd());
			this.engine.currentGameRoom = null;
			this.engine.localClient.clearPlayers();

			return true;
		});

	}

	private void putRoomPreparationCommands(CommandMap map) {
		map.put(CommandType.MsgPlain, sc -> {
			int id = sc.nextByte();
			String message = sc.readToEnd();
			if (id == 0) {

			}
			NetClient client = this.engine.currentGameRoom.getClient(id);
			IChatParticipant p = client.getAnyPlayer();
			this.engine.getChat().addMessage(p != null ? p : Chat.error(), message);
			this.engine.view.updateChat();

			return true;
		});

		map.put(CommandType.MsgSystem, sc -> {
			String message = sc.readToEnd();
			System.out.println(message);
			this.engine.view.alert(message);

			return true;
		});

		map.put(CommandType.RoomSyncState, sc -> {
			byte stateValue = (byte) sc.nextByte();
			GameRoomState state = GameRoomState.getByValue(stateValue);
			this.engine.setCurrentGameRoomState(state);
			switch (state) {
				case Lobby:
					this.engine.setStage(Engine.Stage.Menu);
					this.engine.view.showScene(IView.Scene.Lobby);
					return true;
				case BattleStarting:
					this.engine.view.alert("Wait for other players thx");
					return true;
				case Battle:
					Map tMap = this.engine.currentGameRoom.getWarZone().getMap();
					Player[] players = this.engine.currentGameRoom.getPlayers();
					this.engine.view.setGameData(tMap, players);
					this.engine.setStage(Engine.Stage.Warzone);
					this.engine.view.showScene(IView.Scene.Warzone);
					return true;
			}

			System.err.format("Attempted to change room phase to %s(%d)", state, stateValue);
			return false;
		});

		map.put(CommandType.RoomReadyState, sc -> {
			int readyState = sc.nextByte();
			int clientRID = sc.nextByte();

			NetClient c = this.engine.currentGameRoom.getClient(clientRID);
			if (c == null) {
				return false;
			}
			c.setReady(readyState != 0);
			if (c == this.engine.localClient) {
				this.engine.view.setLocalReadyState(readyState != 0);
			}

			return true;
		});

		map.put(CommandType.RoomClientInfo, sc -> {
			int clientRID = sc.nextByte();
			String name = sc.readToEnd();

			NetClient c = this.engine.currentGameRoom.getClient(clientRID);
			if (c == null) {
				c = new NetClient();
			}
			c.setName(name);
			this.engine.currentGameRoom.setClient(clientRID, c);

			this.engine.view.updateClients();
			return true;
		});

		map.put(CommandType.RoomClientStatus, sc -> {
			NetClient c = this.engine.currentGameRoom.getClient(sc.nextByte());
			c.setStatus(NetClientStatus.getByNumber(sc.nextByte()));
			c.setLatency(sc.nextShort());

			this.engine.view.updateClients();
			return true;
		});

		map.put(CommandType.RoomClientRemove, sc -> {
			int roomId = sc.nextByte();
			String reason = sc.readToEnd();

			NetClient c = this.engine.currentGameRoom.getClient(roomId);

			if (c == this.engine.localClient) {
				return (map.get(CommandType.RoomsLeave)).execute(sc);
			}

			this.engine.currentGameRoom.removeClient(roomId);
			this.engine.currentGameRoom.removePlayersOfClient(c);

			this.engine.view.updateClients();
			this.engine.view.updatePlayers(engine.currentGameRoom.getPlayers());
			return true;
		});

		map.put(CommandType.RoomSetLeader, sc -> {
			int roomId = sc.nextByte();
			NetClient c = this.engine.currentGameRoom.getClient(roomId);
			System.out.println("New room leader is " + c.getName());
			// todo actually change room leadership

			this.engine.view.updateClients();
			return true;
		});

		map.put(CommandType.RoomPlayerAttach, sc -> {
			int playerRoomId = sc.nextByte();
			int playerColor = sc.nextByte();
			int clientRoomId = sc.nextByte();
			int playerClientId = sc.nextByte();

			NetClient c = this.engine.currentGameRoom.getClient(clientRoomId);
			Player p = new Player(c, playerColor);
			c.setPlayer(playerClientId, p);

			this.engine.currentGameRoom.setPlayer(playerRoomId, p);

			this.engine.view.updatePlayers(engine.currentGameRoom.getPlayers());
			return true;
		});

		map.put(CommandType.RoomPlayerDetach, sc -> {
			int playerRID = sc.nextByte();
			if (this.engine.currentGameRoom == null) {
				return false;
			}

			Player p = this.engine.currentGameRoom.removePlayer(playerRID);
			p.getClient().removePlayer(p);
			return true;
		});

		map.put(CommandType.RoomPlayerSetColor, sc -> {
			int roomId = sc.nextByte();
			int playerColor = sc.nextByte();

			return this.engine.currentGameRoom.setPlayerColor(roomId, playerColor);
		});
	}

	private void putRoomGamePlayCommands(CommandMap map) {
		map.put(CommandType.MapSpecification, sc -> {
			int chunkSize = sc.nextByte();
			int xChunks = sc.nextByte();
			int yChunks = sc.nextByte();
			int playerCount = this.engine.currentGameRoom.getPlayerCount();

			System.out.format("Initializing map for %d players\n", playerCount);

			Map tunnelerMap = new Map(chunkSize, xChunks, yChunks, playerCount);
			WarZone zone = new WarZone(new WarZoneRules(), playerCount);
			zone.setMap(tunnelerMap);
			this.engine.currentGameRoom.setWarZone(zone);

			this.remainingChunks = xChunks * yChunks;
			return true;
		});

		map.put(CommandType.MapBases, sc -> {
			Map tunnelerMap = this.engine.currentGameRoom.getWarZone().getMap();
			IntPoint[] bases;

			int n = sc.nextByte();
			bases = new IntPoint[n];

			for (int i = 0; i < n; i++) {
				int x = sc.nextByte();
				int y = sc.nextByte();
				short playerRID = sc.nextByte();
				Player p = this.engine.currentGameRoom.getPlayer(playerRID);
				IntPoint location = tunnelerMap.setPlayerBaseChunk(i, new IntPoint(x, y), p);
				this.engine.currentGameRoom.getWarZone().initTank(playerRID, p, location);
			}

			return true;
		});

		map.put(CommandType.MapChunkData, sc -> {
			int chunkX = sc.nextByte();
			int chunkY = sc.nextByte();
			int checkSum = sc.nextByte();
			Block[] chunkData = this.mapChunkParser.parseData(sc);
			try {
				if (!this.engine.currentGameRoom.getWarZone().getMap()
						.updateChunk(chunkX, chunkY, chunkData)) {
					System.out.format("Errors occured while updating chunk x=%d, y=%d\n", chunkX, chunkY);
				}
				this.remainingChunks--;
				if (this.remainingChunks == 0) {
					Command rdyCommand = this.engine.netadapter.createCommand(CommandType.RoomReadyState);
					rdyCommand.append((byte) 1);
					this.engine.netadapter.send(rdyCommand);
				} else if (this.remainingChunks < 0) {
					System.err.format("Received %d too many chunks\n", -this.remainingChunks);
				}
				return true;
			} catch (ChunkException ex) {
				System.err.println("Chunk data error: " + ex);
				return false;
			}

		});

		map.put(CommandType.MapBlocksChanges, sc -> {
			Map tunnelerMap = this.engine.currentGameRoom.getWarZone().getMap();

			int n = sc.nextByte();
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

			Player p = this.engine.currentGameRoom.getPlayer(roomId);
			p.getControls().setState(newState);

			return true;
		});

		map.put(CommandType.GameTankInfo, sc -> {
			if (this.engine.currentGameRoom == null) {
				return false;
			}
			int roomId = sc.nextByte();
			Tank.Status status = Tank.Status.get(sc.nextByte());
			int x = sc.nextShort();
			int y = sc.nextShort();
			Direction direction = Direction.fromByteValue((byte) sc.nextByte());
			int hitPoints = sc.nextByte();
			int energy = sc.nextByte();

			if (this.engine.currentGameRoom.getState() != GameRoomState.Battle) {
				System.err.println("Attempted to initialize tank info while state is not Battle");
				return false;
			}

			Tank t = this.engine.currentGameRoom.getWarZone().getTank(roomId);
			t.setStatus(status);
			t.setLocation(x, y);
			t.setDirection(direction);
			t.setHitPoints(hitPoints);
			t.setEnergy(energy);

			return true;
		});

		map.put(CommandType.GameProjAdd, sc -> {
			int n = sc.nextByte();
			int playerRoomId = sc.nextByte();
			int x = sc.nextShort();
			int y = sc.nextShort();
			Direction direction = Direction.fromByteValue((byte) sc.nextByte());

			Player p = this.engine.currentGameRoom.getPlayer(playerRoomId);
			Tank t = this.engine.currentGameRoom.getWarZone().getTank(playerRoomId);

			this.engine.currentGameRoom.getWarZone().setProjectile(n, new IntPoint(x, y), direction, p);
			t.setCoolDown(this.engine.currentGameRoom.getWarZone().getRules().getTankCannonCooldown());

			return true;
		});

		map.put(CommandType.GameProjRem, sc -> {
			int n = sc.nextByte();
			this.engine.currentGameRoom.getWarZone().removeProjectile(n);
			return true;
		});
	}
}

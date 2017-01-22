package tunnelers.core.gameRoom;

import tunnelers.core.player.Player;
import tunnelers.core.chat.Chat;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.map.Map;
import tunnelers.core.model.entities.Tank;
import tunnelers.network.NetClient;

public class GameRoom {

	private GameRoomState state;

	private int leaderClientRID;
	private final NetClient[] clients;
	private final Player[] players;

	private final Chat chat;

	private Warzone warzone;

	private final int projectileCapacity;

	public GameRoom(int leaderRID, int capacity, int chatCapacity, int projectilesCapacity) {
		this.state = GameRoomState.Idle;

		this.clients = new NetClient[capacity];
		this.players = new Player[capacity];
		this.chat = new Chat(chatCapacity);

		this.projectileCapacity = projectilesCapacity;
		this.leaderClientRID = leaderRID;
	}

	public void setMap(Map map) {
		this.warzone = new Warzone(new WarzoneRules());
		this.warzone.setMap(map);
	}

	public void initWarzone() {
		Tank[] tanks = new Tank[players.length];
		WarzoneRules warzoneRules = this.getWarzone().getRules();
		int baseIndex = 0;
		for (int i = 0; i < players.length; i++) {
			Player p = players[i];
			if (p == null) {
				continue;
			}
			IntPoint baseCenter = this.warzone.getMap().assignBase(baseIndex++, p);
			Tank tank = new Tank(p, baseCenter,
					warzoneRules.getTankMaxHP(), warzoneRules.getTankMaxEP());
			tanks[i] = tank;
		}
		

		this.warzone.setTanks(tanks);
	}

	public GameRoomState getState() {
		return state;
	}

	public void setState(GameRoomState state) {
		this.state = state;
	}

	public int getCapacity() {
		return this.players.length;
	}

	public int getPlayerCount() {
		int count = 0;
		for (Player p : this.players) {
			if (p != null) {
				count++;
			}
		}
		return count;
	}

	public NetClient getLeaderClient() {
		return this.getClient(this.leaderClientRID);
	}

	public NetClient getClient(int roomId) {
		if (roomId < 0 || roomId >= clients.length) {
			throw new GameRoomIndexException(0, clients.length - 1, roomId);
		}
		return this.clients[roomId];
	}

	public void setClient(int roomId, NetClient client) {
		if (roomId < 0 || roomId >= clients.length) {
			throw new GameRoomIndexException(0, clients.length - 1, roomId);
		}
		this.clients[roomId] = client;
	}

	public NetClient removeClient(int roomId) {
		NetClient client = this.getClient(roomId);
		this.setClient(roomId, null);

		return client;
	}

	public void removePlayersOfClient(NetClient client) {
		if (client == null) {
			return;
		}

		for (int i = 0; i < this.getCapacity(); i++) {
			if (this.players[i] != null && this.players[i].getClient() == client) {
				this.removePlayer(i);
			}
		}
	}

	public Player getPlayer(int roomId) {
		if (roomId < 0 || roomId >= clients.length) {
			throw new GameRoomIndexException(0, clients.length - 1, roomId);
		}

		return this.players[roomId];
	}

	public void setPlayer(int roomId, Player player) {
		if (roomId < 0 || roomId >= clients.length) {
			throw new GameRoomIndexException(0, clients.length - 1, roomId);
		}

		this.players[roomId] = player;
	}

	public Player removePlayer(int roomId) {
		Player p = this.getPlayer(roomId);
		this.setPlayer(roomId, null);

		return p;
	}

	public Warzone getWarzone() {
		return this.warzone;
	}

	public Player[] getPlayers() {
		return this.players;
	}

	public Chat getChat() {
		return this.chat;
	}

	public Map getMap() {
		return this.warzone.getMap();
	}

	public Projectile[] getProjectiles() {
		return this.warzone.getProjectiles();
	}

	public Tank[] getTanks() {
		return this.warzone.getTanks();
	}

	public int getLeaderClientRID() {
		return leaderClientRID;
	}

	public void setLeaderClientRID(int leaderClientRID) {
		this.leaderClientRID = leaderClientRID;
	}
}

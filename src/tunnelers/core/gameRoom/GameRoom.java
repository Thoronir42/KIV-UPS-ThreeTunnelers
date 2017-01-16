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

	public void initWarzone(Map map) {
		Tank[] tanks = new Tank[players.length];

		int baseIndex = 0;
		for (int i = 0; i < players.length; i++) {
			Player p = players[i];
			if (p == null) {
				continue;
			}
			IntPoint baseCenter = map.assignBase(baseIndex++, p);
			Tank tank = new Tank(p, baseCenter);
			tanks[i] = tank;
		}

		this.warzone = new Warzone(tanks, map, this.projectileCapacity);
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

	public NetClient getLeaderClient(){
		return this.getClient(this.leaderClientRID);
	}
	
	public NetClient getClient(int roomId) {
		if (roomId < 1 || roomId > clients.length) {
			throw new GameRoomIndexException(1, clients.length, roomId);
		}
		return this.clients[roomId - 1];
	}

	public void setClient(int roomId, NetClient client) {
		if (roomId < 1 || roomId > clients.length) {
			throw new GameRoomIndexException(1, clients.length, roomId);
		}
		this.clients[roomId - 1] = client;
	}

	public NetClient removeClient(int roomId) {
		NetClient client = this.getClient(roomId);
		this.setClient(roomId, null);

		return client;
	}

	public Player getPlayer(int roomId) {
		if (roomId < 1 || roomId > clients.length) {
			throw new GameRoomIndexException(1, clients.length, roomId);
		}
		
		return this.players[roomId - 1];
	}

	public void setPlayer(int roomId, Player player) {
		if (roomId < 1 || roomId > clients.length) {
			throw new GameRoomIndexException(1, clients.length, roomId);
		}
		
		this.players[roomId - 1] = player;
	}
	
	public Player removePlayer(int roomId){
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

package tunnelers.core.gameRoom;

import generic.IndexNotInRangeException;
import tunnelers.core.chat.Chat;
import tunnelers.core.player.Player;
import tunnelers.network.NetClient;

public class GameRoom {

	private GameRoomState state;

	private int leaderClientRID;
	private final NetClient[] clients;
	private final Player[] players;

	private final Chat chat;

	private WarZone warZone;

	public GameRoom(int leaderRID, int capacity, int chatCapacity) {
		this.state = GameRoomState.Idle;

		this.clients = new NetClient[capacity];
		this.players = new Player[capacity];
		this.chat = new Chat(chatCapacity);
		this.leaderClientRID = leaderRID;
	}

	public WarZone getWarZone() {
		return this.warZone;
	}

	public void setWarZone(WarZone warZone) {
		this.warZone = warZone;
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
			throw new IndexNotInRangeException(0, clients.length - 1, roomId);
		}
		return this.clients[roomId];
	}

	public void setClient(int roomId, NetClient client) {
		if (roomId < 0 || roomId >= clients.length) {
			throw new IndexNotInRangeException(0, clients.length - 1, roomId);
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
			throw new IndexNotInRangeException(0, clients.length - 1, roomId);
		}

		return this.players[roomId];
	}

	public void setPlayer(int roomId, Player player) {
		if (roomId < 0 || roomId >= players.length) {
			throw new IndexNotInRangeException(0, players.length - 1, roomId);
		}

		this.players[roomId] = player;
	}

	public void initPlayer(int roomId, int clientRoomId, int playerClientId, int playerColor) {
		if(clientRoomId < 0 || clientRoomId > clients.length || clients[clientRoomId] == null) {
			throw new IllegalStateException("Client " + clientRoomId + " is not present");
		}
		NetClient client = clients[clientRoomId];

		this.players[roomId] = new Player(client, playerColor);
		client.setPlayer(playerClientId, roomId);
	}

	public Player removePlayer(int roomId) {
		Player p = this.getPlayer(roomId);
		this.setPlayer(roomId, null);
		p.getClient().removePlayer(roomId);

		return p;
	}


	public Player[] getPlayers() {
		return this.players;
	}

	public Chat getChat() {
		return this.chat;
	}

	public int getLeaderClientRID() {
		return leaderClientRID;
	}

	public void setLeaderClientRID(int leaderClientRID) {
		this.leaderClientRID = leaderClientRID;
	}

	public NetClient[] getClients() {
		return this.clients;
	}

	public boolean setPlayerColor(int roomId, int color) {
		if (roomId < 0 || roomId > this.players.length || this.players[roomId] == null) {
			return false;
		}

		// todo: mark color as used
		this.players[roomId].setColor(color);
		return true;
	}
}

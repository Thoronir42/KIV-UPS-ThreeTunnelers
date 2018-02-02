package tunnelers.network;

import tunnelers.core.player.Player;

public class NetClient {

	public static int MAX_NAME_LENGTH = 12;
	public static final int PLAYER_CAPACITY = 2;

	private int latency;
	private String name;
	private final Player[] players;
	private int activePlayers;
	private boolean ready;
	private NetClientStatus status;

	public NetClient() {
		this("");
	}

	public NetClient(String name) throws IllegalArgumentException {
		this.setName(name);
		this.players = new Player[PLAYER_CAPACITY];
		this.activePlayers = 0;
		this.ready = false;
		this.status = NetClientStatus.Connected;
	}

	public String getName() {
		return name;
	}

	public String getName(Player p) {
		if (this.activePlayers < 2) {
			return this.name;
		}
		int i = 0;
		for (Player check : this.players) {
			i++;
			if (check.equals(p)) {
				return String.format("%s[%d]", this.name, i);
			}
		}
		return this.name + "[?]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlayer(int n, Player p) {
		this.players[n] = p;

		this.recountPlayers();
	}

	public Player getAnyPlayer() {
		for (Player p : this.players) {
			if (p != null) {
				return p;
			}
		}

		return null;
	}

	public Player getPlayer(int n) {
		return this.players[n];
	}

	public void removePlayer(Player p) {
		for (int i = 0; i < this.players.length; i++) {
			if (this.players[i] == p) {
				this.setPlayer(i, null);
				return;
			}
		}
	}

	public void clearPlayers() {
		for (int i = 0; i < this.players.length; i++) {
			this.setPlayer(i, null);
		}
	}

	private void recountPlayers() {
		int count = 0;
		for (Player player : this.players) {
			if (player != null) {
				count++;
			}
		}
		this.activePlayers = count;
	}

	public int getLatency() {
		return latency;
	}

	public void setLatency(int latency) {
		this.latency = latency;
	}

	public NetClientStatus getStatus() {
		return status;
	}

	public void setStatus(NetClientStatus status) {
		this.status = status;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

}

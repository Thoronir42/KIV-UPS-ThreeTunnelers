package tunnelers.network;

import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class NetClient {

	public static int MAX_NAME_LENGTH = 12;
	public static int PLAYER_CAPACITY = 2;

	private int latency;
	private boolean connected;
	private String name;
	private final Player[] players;
	private int activePlayers;
	private boolean ready;

	public NetClient() {
		this("");
	}

	public NetClient(String name) throws IllegalArgumentException {
		this.setName(name);
		this.players = new Player[PLAYER_CAPACITY];
		this.activePlayers = 0;
		this.ready = false;
	}

	public String getName() {
		return name;
	}

	public String getName(Player p) {
		String name = this.getName();
		if (this.activePlayers < 2) {
			return name;
		}
		int i = 0;
		for (Player check : this.players) {
			i++;
			if (check.equals(p)) {
				return String.format("%s[%d]", name, i);
			}
		}
		return name + "[?]";
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
			if(this.players[i] == p){
				this.setPlayer(i, null);
				return;
			}
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

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

}

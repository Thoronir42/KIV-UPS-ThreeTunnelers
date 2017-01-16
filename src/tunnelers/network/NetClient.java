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

	public void setName(String name) throws IllegalArgumentException {
		if (name.length() > MAX_NAME_LENGTH) {
			throw new IllegalArgumentException("Name length must be less or equal to " + MAX_NAME_LENGTH);
		}
		this.name = name;
	}

	public void setPlayer(int n, Player p) {
		this.players[n] = p;

		int count = 0;
		for (Player player : this.players) {
			if (player != null) {
				count++;
			}
		}
		this.activePlayers = count;
	}

	public Player getAnyPlayer() {
		for(Player p : this.players){
			if(p != null){
				return p;
			}
		}
		
		return null;
	}
	
	public Player getPlayer(int n) {
		return this.players[n];
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

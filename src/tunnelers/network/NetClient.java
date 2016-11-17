package tunnelers.network;

import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class NetClient {

	public static int MAX_NAME_LENGTH = 12;
	public static int PLAYER_CAPACITY = 2;

	private String name;
	private final Player[] players;
	private int activePlayers;

	public NetClient() {
		this("");
	}

	public NetClient(String name) throws IllegalArgumentException {
		this.setName(name);
		this.players = new Player[PLAYER_CAPACITY];
		this.activePlayers = 0;
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

	public Player getPlayer(int n) {
		return this.players[n];
	}

}

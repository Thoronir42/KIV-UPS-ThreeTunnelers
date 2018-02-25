package tunnelers.network;

public class NetClient {

	public static int MAX_NAME_LENGTH = 12;

	private int latency;
	private String name;
	private final int[] playerRids;
	private int activePlayers;
	private boolean ready;
	private Status status;

	public NetClient() {
		this("", 1);
	}

	public NetClient(String name) {
		this(name, 1);
	}

	public NetClient(String name, int playerCapacity) throws IllegalArgumentException {
		this.setName(name);
		this.playerRids = new int[playerCapacity];
		for (int i = 0; i < playerCapacity; i++) {
			playerRids[i] = -1;
		}
		this.activePlayers = 0;
		this.ready = false;
		this.status = Status.Connected;
	}

	public String getName() {
		return name;
	}

	public String getName(int p) {
		if (this.activePlayers < 2) {
			return this.name;
		}
		int i = 0;
		for (int check : this.playerRids) {
			i++;
			if (check == p) {
				return String.format("%s[%d]", this.name, i);
			}
		}
		return this.name + "[?]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlayer(int n, int p) {
		this.playerRids[n] = p;

		this.recountPlayers();
	}

	public int getAnyPlayerRID() {
		for (int p : this.playerRids) {
			if (p != -1) {
				return p;
			}
		}

		return -1;
	}

	public int getPlayer(int n) {
		return this.playerRids[n];
	}

	public void removePlayer(int p) {
		for (int i = 0; i < this.playerRids.length; i++) {
			if (this.playerRids[i] == p) {
				this.setPlayer(i, -1);
				return;
			}
		}
	}

	public void clearPlayers() {
		for (int i = 0; i < this.playerRids.length; i++) {
			this.setPlayer(i, -1);
		}
	}

	private void recountPlayers() {
		int count = 0;
		for (int player : this.playerRids) {
			if (player != -1) {
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public enum Status {
		Connected, Playing, Disconnected;

		public static Status getByNumber(int n) {
			switch (n) {
				case 1:
					return Connected;
				case 2:
					return Playing;
				default:
				case 4:
					return Disconnected;
			}
		}
	}
}

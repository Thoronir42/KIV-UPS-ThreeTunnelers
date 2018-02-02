package tunnelers.network;

public enum NetClientStatus {
	Connected, Playing, Disconnected;

	public static NetClientStatus getByNumber(int n) {
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

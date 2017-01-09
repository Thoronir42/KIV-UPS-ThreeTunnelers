package tunnelers.app.views.serverList;

/**
 *
 * @author Stepan
 */
public enum GameRoomDifficulty {
	Easy("Lehká", 1),
	Medium("Střední", 2),
	Hard("Těžká", 3),
	Unspecified("Nespecifikována", 0);

	private final String label;
	private final int value;

	private GameRoomDifficulty(String label, int value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Obtížnost " + label;
	}

	public int intValue() {
		return value;
	}
}

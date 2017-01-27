package tunnelers.app.views.serverList;

/**
 *
 * @author Stepan
 */
public enum GameMode {
	FFA("FFA", 1),
	Unspecified("N/A", 0);

	private final String label;
	private final int value;

	private GameMode(String label, int value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public String toString() {
		return label;
	}

	public int intValue() {
		return value;
	}
}

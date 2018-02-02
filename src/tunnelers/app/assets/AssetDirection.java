package tunnelers.app.assets;

public enum AssetDirection {
	Upward(0), Diagonal(1);

	private final int order;

	AssetDirection(int order) {

		this.order = order;
	}

	public int getOrder() {
		return order;
	}
}

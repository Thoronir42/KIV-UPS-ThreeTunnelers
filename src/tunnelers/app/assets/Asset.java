package tunnelers.app.assets;

public enum Asset {
	TankBody(0),
	TankBodyDiag(1),
	TankCannon(2),
	TankCannonDiag(3),
	Projectile(4),
	ProjectileDiag(5);

	private final int order;

	Asset(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	public static int count() {
		return 6;
	}
}

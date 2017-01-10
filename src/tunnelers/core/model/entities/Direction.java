package tunnelers.core.model.entities;

/**
 *
 * @author Stepan
 */
public enum Direction {

	North(0, -1, false, 3), NorthEast(1, -1, true, 0),
	East(1, 0, false, 0), SouthEast(1, 1, true, 1),
	South(0, 1, false, 1), SouthWest(-1, 1, true, 2),
	West(-1, 0, false, 2), NorthWest(-1, -1, true, 3);

	private static final Direction[][] alignMent = {
		{NorthWest, North, NorthEast},
		{West, null, East},
		{SouthWest, South, SouthEast}
	};

	public static Direction getDirection(int X, int Y) {
		if ((X < -1 || X > 1) || (Y < -1 || Y > 1)) {
			return null;
		}
		return alignMent[Y + 1][X + 1];
	}

	private final IntPoint direction;
	private final boolean diagonal;
	private final int rotation;

	private Direction(int x, int y, boolean offset, int rotation) {
		this.direction = new IntPoint(x, y);
		this.diagonal = offset;
		this.rotation = rotation;
	}

	public boolean isDiagonal() {
		return this.diagonal;
	}

	public int getRotation() {
		return this.rotation;
	}

	public IntPoint asPoint() {
		return this.direction;
	}

	public int getX() {
		return this.direction.getX();
	}

	public int getY() {
		return this.direction.getY();
	}
}

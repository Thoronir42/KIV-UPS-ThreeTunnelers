package tunnelers.core.model.entities;

import java.util.HashMap;

public enum Direction {
	Undefined(0, 0, 0, false),
	North(1, 0, -1, false), NorthEast(2, 1, -1, true),
	East(3, 1, 0, false), SouthEast(4, 1, 1, true),
	South(5, 0, 1, false), SouthWest(6, -1, 1, true),
	West(7, -1, 0, false), NorthWest(8, -1, -1, true);

	private final byte byteVal;
	private final IntPoint direction;
	private final boolean diagonal;

	Direction(int byteVal, int x, int y, boolean diagonal) {
		this.byteVal = (byte) byteVal;
		this.direction = new IntPoint(x, y);
		this.diagonal = diagonal;
	}

	public boolean isDiagonal() {
		return this.diagonal;
	}

	public byte byteValue() {
		return this.byteVal;
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


	// todo: possibly move to separate class

	private static final Direction[][] alignment = {
			{NorthWest, North, NorthEast},
			{West, null, East},
			{SouthWest, South, SouthEast}
	};
	private static final HashMap<Byte, Direction> typeMap = createMap();

	public static Direction fromByteValue(byte c) {
		return typeMap.getOrDefault(c, Undefined);
	}

	private static HashMap<Byte, Direction> createMap() {
		HashMap<Byte, Direction> typeMap = new HashMap<>();
		for (Direction type : Direction.values()) {
			typeMap.put(type.byteValue(), type);
		}

		return typeMap;
	}

	public static Direction getDirection(int X, int Y) {
		X = (int) Math.signum(X);
		Y = (int) Math.signum(Y);
		return alignment[Y + 1][X + 1];
	}

}

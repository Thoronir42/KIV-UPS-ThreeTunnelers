package tunnelers.Game.IO;

import tunnelers.Game.Frame.Direction;

public enum Input {

	movUp(Direction.North, 0),
	movDown(Direction.South, 1),
	movLeft(Direction.West, 2),
	movRight(Direction.East, 3),
	actShoot(4);

	private final Direction dir;
	private final int intVal;

	private Input(int intVal) {
		this(null, intVal);
	}

	private Input(Direction dir, int intVal) {
		this.dir = dir;
		this.intVal = intVal;
	}

	public Direction getDirection() {
		return this.dir;
	}

	public double getX() {
		return this.dir.getX();
	}

	public double getY() {
		return this.dir.getY();
	}

	public int intVal() {
		return this.intVal;
	}

}

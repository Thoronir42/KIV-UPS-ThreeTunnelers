package tunnelers.Game.IO;

import tunnelers.Game.Frame.Direction;

public enum Input {

	movUp(Direction.North, 0, "Nahoru"),
	movDown(Direction.South, 1, "Dolů"),
	movLeft(Direction.West, 2, "Doleva"),
	movRight(Direction.East, 3, "Doprava"),
	actShoot(4, "Střílet");

	private final Direction dir;
	private final int intVal;
	private final String label;

	private Input(int intVal, String label) {
		this(null, intVal, label);
	}

	private Input(Direction dir, int intVal, String label) {
		this.dir = dir;
		this.intVal = intVal;
		this.label = label;
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

	public String getLabel() {
		return label;
	}
	

}

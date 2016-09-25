package tunnelers.Game.IO;

import tunnelers.model.entities.Direction;

public enum InputAction {

	movUp(0, "Nahoru", Direction.North),
	movDown(1, "Dolů", Direction.South),
	movLeft(2, "Doleva", Direction.West),
	movRight(3, "Doprava", Direction.East),
	actShoot(4, "Střílet");

	private final Direction dir;
	private final int intVal;
	private final String label;

	private InputAction(int intVal, String label) {
		this(intVal, label, null);
	}

	private InputAction(int intVal, String label, Direction dir) {
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

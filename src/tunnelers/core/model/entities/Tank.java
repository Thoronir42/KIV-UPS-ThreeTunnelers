package tunnelers.core.model.entities;

import tunnelers.core.player.Player;

public class Tank extends GameEntity {

	private Status status;

	private int hitPoints, energy;

	private int cannonCoolDown;

	public Tank(Player player, IntPoint initialLocation, int hitpoints, int energy) {
		super(initialLocation, Direction.North, player);

		this.status = Status.Operative;
		this.hitPoints = hitpoints;
		this.energy = energy;
		this.cannonCoolDown = 0;
	}

	@Override
	public IntDimension getSize() {
		return ShapeFactory.get(direction, ShapeFactory.Type.TankBelt).getSize();
	}

	public void changeDirection(Direction d) {
		this.direction = d;
	}

	public void setCoolDown(int coolDown) {
		this.cannonCoolDown = coolDown;
	}

	public boolean cooldown(int coolDownRate) {
		int newValue = this.cannonCoolDown - coolDownRate;
		this.cannonCoolDown = newValue < 0 ? 0 : newValue;
		return this.cannonCoolDown == 0;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitpoints) {
		this.hitPoints = hitpoints;
	}

	public int getEnergy() {
		return this.energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public enum Status {
		Operative, Destroyed;

		public static Status get(int n) {
			if (n == 1) {
				return Operative;
			}
			return Destroyed;
		}
	}
}

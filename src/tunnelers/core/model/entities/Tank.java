package tunnelers.core.model.entities;

import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Tank extends GameEntity {

	protected Status status;
	
	protected int hitpoints, energy;

	protected int cannonCooldown;

	public Tank(Player player, IntPoint initialLocation, int hitpoints, int energy) {
		super(initialLocation, Direction.North, player);
		
		this.status = Status.Operative;
		this.hitpoints = hitpoints;
		this.energy = energy;
		this.cannonCooldown = 0;
	}

	@Override
	public IntDimension getSize() {
		return ShapeFactory.get(direction, ShapeFactory.Type.TankBelt).getSize();
	}

	public void changeDirection(Direction d) {
		this.direction = d;
	}

	public void setCooldown(int cooldown) {
		this.cannonCooldown = cooldown;
	}

	public boolean cooldown(int cooldownRate) {
		int newValue = this.cannonCooldown - cooldownRate;
		this.cannonCooldown = newValue < 0 ? 0 : newValue;
		return this.cannonCooldown == 0;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public void setHitPoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	public int getEnergy() {
		return this.energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public static enum Status {
		Operative, Destroyed
	}
}

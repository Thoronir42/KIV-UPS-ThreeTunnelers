package tunnelers.core.model.entities;

import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Tank extends GameEntity {

	public static final IntDimension SIZE = new IntDimension(7, 7);

	protected int hitpoints, energy;

	protected int cannonCooldown;

	public Tank(Player player, IntPoint initialLocation, int hitpoints, int energy) {
		super(initialLocation, Direction.North, player);
		this.hitpoints = hitpoints;
		this.energy = energy;
		this.cannonCooldown = 0;
	}

	@Override
	public IntDimension getSize() {
		return SIZE;
	}

	public void changeDirection(Direction d) {
		this.direction = d;
	}

	public void setCooldown(int cooldown){
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
}

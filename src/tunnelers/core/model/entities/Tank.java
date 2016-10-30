package tunnelers.core.model.entities;

import tunnelers.core.player.Player;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

/**
 *
 * @author Stepan
 */
public class Tank extends GameEntity {

	public static final Dimension2D SIZE = new Dimension2D(7, 7);

	public static final int MAX_HITPOINTS = 20,
			MAX_ENERGY = 250,
			CANNON_COOLDOWN = 5;

	protected int hitpoints, energy;

	protected int cannonCooldown;

	public Tank(Player player, Point2D initialLocation) {
		super(Direction.North, initialLocation, player);
		this.hitpoints = MAX_HITPOINTS;
		this.energy = MAX_ENERGY;

		this.cannonCooldown = 0;
	}

	@Override
	public Dimension2D getSize() {
		return SIZE;
	}

	public void changeDirection(Direction d) {
		this.direction = d;
	}

	@Override
	public int update() {
		if (this.cannonCooldown > 0) {
			this.cannonCooldown--;
		}
		return 0;
	}

	public Point2D tryShoot() {
		if (this.cannonCooldown <= 0) {
			this.cannonCooldown = CANNON_COOLDOWN;
			return this.location;
		}
		return null;
	}

	public double getHitpointsPct() {
		return 1.0 * this.hitpoints / MAX_HITPOINTS;
	}

	public double getHitpoints() {
		return hitpoints;
	}

	public void setHitPoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	public double getEnergyPct() {
		return 1.0 * energy / MAX_ENERGY;
	}

	public int getEnergy() {
		return this.energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}
}

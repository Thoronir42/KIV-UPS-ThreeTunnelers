package tunnelers.model.entities;

import tunnelers.model.player.APlayer;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class Tank extends GameEntity {

	public static final Dimension2D SIZE = new Dimension2D(7, 7);

	public static final int 
			MAX_HITPOINTS = 20,
			MAX_ENERGY = 250,
			CANNON_COOLDOWN = 5;

	protected double hitPoints, energyStatus;
	
	protected int cannonCooldown;

	public Tank(APlayer player, Point2D initialLocation) {
		super(Direction.North, initialLocation, player);
		this.hitPoints = MAX_HITPOINTS;
		this.energyStatus = MAX_ENERGY;
		
		this.cannonCooldown = 0;
	}

	@Override
	public Dimension2D getSize() {
		return SIZE;
	}

	public Color getColor() {
		return this.player.getColor();
	}

	public void changeDirection(Direction d) {
		this.direction = d;
	}
	
	@Override
	public int update() {
		if(this.cannonCooldown > 0){
			this.cannonCooldown--;
		}
		return 0;
	}
	
	public Point2D tryShoot(){
		if(this.cannonCooldown <= 0 ){
			this.cannonCooldown = CANNON_COOLDOWN;
			return this.location;
		}
		return null;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public double getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(double hitPoints) {
		this.hitPoints = hitPoints;
	}

	public double getEnergyStatus() {
		return energyStatus;
	}

	public void setEnergyStatus(double energyStatus) {
		this.energyStatus = energyStatus;
	}
}

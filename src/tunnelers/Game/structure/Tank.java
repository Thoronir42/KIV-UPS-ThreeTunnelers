package tunnelers.Game.structure;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import tunnelers.Assets;
import tunnelers.Game.TunColors;

/**
 *
 * @author Stepan
 */
public class Tank extends GameEntity{

	public static final Dimension2D SIZE = new Dimension2D(7, 7);
    private Image iv_body_regular, iv_body_diagonal,
			iv_cannon_regular, iv_cannon_diagonal;
	
	
    public static int MAX_HITPOINTS = 20,
						MAX_ENERGY = 120;
    
	
    protected double hitPoints, energyStatus;
	
    public Tank(Player player, Point2D initialLocation){
        super(Direction.North, initialLocation, player);
        this.hitPoints = MAX_HITPOINTS;
        this.energyStatus = MAX_ENERGY;
    }
    
	@Override
	public Dimension2D getSize(){
		return SIZE;
	}
	
    public Color getColor(){
        return this.player.getColor();
    }
    
    public void changeDirection(Direction d){
        this.direction = d;
    }
    
    @Override
    public int update() {
        
        return 0;
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

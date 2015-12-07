package tunnelers.Game.structure;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import tunnelers.Assets;

/**
 *
 * @author Stepan
 */
public class Tank extends GameEntity{

	public static final Dimension2D TANK_SIZE = new Dimension2D(7, 7);
    private Image iv_body_regular, iv_body_diagonal,
			iv_cannon_regular, iv_cannon_diagonal;
	
	
    public static int MAX_HITPOINTS = 20,
						MAX_ENERGY = 120;
    
	
    protected double hitPoints, energyStatus;
	protected Image[] playerTank = new Image[4];
	
    public Tank(Player player, Point2D initialLocation){
        super(Direction.North, initialLocation, player);
        this.hitPoints = MAX_HITPOINTS;
        this.energyStatus = MAX_ENERGY;
		
		this.initImages();
    }
	
	private void initImages(){
		this.iv_body_regular = Assets.getImage(Assets.TANK_BODY, this.getColor());
		iv_body_diagonal = Assets.getImage(Assets.TANK_BODY_DIAG, this.getColor());
		iv_cannon_regular = Assets.getImage(Assets.TANK_CANNON, this.getColor());
		iv_cannon_diagonal= Assets.getImage(Assets.TANK_CANNON_DIAG, this.getColor());
    }
    
	@Override
	public Dimension2D getSize(){
		return TANK_SIZE;
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

	@Override
	public void draw(GraphicsContext g, Dimension2D d) {
		Image iv_body = this.getBodyImage();
		Image iv_cannon = this.getCannonImage();
		
		double degrees = this.direction.getRotation() * 90;
		int dx = (int)(1+ TANK_SIZE.getWidth() / 2),
			dy = (int)(1+ TANK_SIZE.getHeight() / 2);
		
		g.rotate(degrees);
		g.drawImage(iv_body, -dx*d.getWidth(), -dy*d.getHeight(),
			TANK_SIZE.getWidth()*d.getWidth(), TANK_SIZE.getHeight() * d.getHeight());
		g.drawImage(iv_cannon, -dx*d.getWidth(), -dy*d.getHeight(),
			TANK_SIZE.getWidth()*d.getWidth(), TANK_SIZE.getHeight() * d.getHeight());
	}
	
	private Image getBodyImage(){
		return this.direction.isDiagonal() ? iv_body_diagonal : iv_body_regular;
	}
	private Image getCannonImage(){
		return this.direction.isDiagonal() ? iv_cannon_diagonal:iv_cannon_regular;
	}
	
	
}

package tunnelers.core.model.entities;

import tunnelers.core.player.Player;
import javafx.geometry.Dimension2D;
import tunnelers.core.colors.IColorable;

/**
 *
 * @author Stepan
 */
public abstract class GameEntity  implements IColorable{
	protected Direction direction;
	protected IntPoint location;
	protected Player player;

	public GameEntity(Direction direction, IntPoint location, Player player) {
		this.direction = direction;
		this.player = player;
		this.setLocation(location);
		
	}

	public abstract Dimension2D getSize();

	public IntPoint getLocation() {
		return this.location;
	}

	public void setLocation(IntPoint loc) {
		this.location = loc;
	}

	public double getX() {
		return this.location.getX();
	}

	public double getY() {
		return this.location.getY();
	}

	public double getWidth() {
		return this.getSize().getWidth();
	}

	public double getHeight() {
		return this.getSize().getHeight();
	}

	public double getTopBorder() {
		return this.getY() - (this.getHeight() - 1) / 2;
	}

	public double getLeftBorder() {
		return this.getX() - (this.getWidth() - 1) / 2;
	}

	public double getRightBorder() {
		return this.getX() + (this.getWidth() - 1) / 2;
	}

	public double getBottomBorder() {
		return this.getY() + (this.getHeight() - 1) / 2;
	}

	public Player getPlayer() {
		return this.player;
	}

	@Override
	public int getColor() {
		return this.player.getColor();
	}
	
	public int getPlayerId(){
		return this.player.getID();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}

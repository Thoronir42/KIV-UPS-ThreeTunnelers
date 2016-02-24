package tunnelers.Game.Frame;

import javafx.geometry.Point2D;
import tunnelers.Settings;
import javafx.scene.paint.Color;

/**
 *
 * @author Stepan
 */
public class Player {

	private static int num_of_instances = 0;
	private final static Settings settings = Settings.getInstance();

	private final int playerID;
	private final String name;
	private final Controls controlScheme;
	private Color color;
	private Tank tank;

	public Player(String name) {
		//public Player(String name) throws PlayerException{
		int newID = num_of_instances + 1;
		if (newID > Settings.MAX_PLAYERS) {
			//throw new PlayerException(newID);
		}
		this.playerID = num_of_instances = newID;
		this.name = name;
		this.setColor(newID - 1);
		this.controlScheme = new Controls();
	}

	public Color getColor() {
		return this.color;
	}

	public final void setColor(int colorId) {
		this.color = settings.getColor(this.color, colorId);
	}

	public String getName() {
		return this.name;
	}

	public int getID() {
		return this.playerID;
	}

	@Override
	public String toString() {
		return String.format("[%2d] %16s (%s)", this.playerID, this.name, this.color);
	}

	public Point2D getLocation() {
		return this.tank.getLocation();
	}

	public void setLocation(Point2D loc) {
		this.tank.setLocation(loc);
		System.out.println(name + " new location set: " + loc.toString());
	}

	public void setTank(Tank t) {
		if(this.tank != null){
			System.err.println("Re-assigning player tank");
		}
		this.tank = t;
	}

	public Tank getTank() {
		return this.tank;
	}

	public Controls getControls() {
		return this.controlScheme;
	}
}

class PlayerException extends Exception {

	public PlayerException(int n) {
		super(String.format("Tried to create %d. player, while the player limit is %d.", n, Settings.MAX_PLAYERS));
	}
}

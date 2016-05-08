package tunnelers.Game.Frame;

import tunnelers.Settings;
import javafx.scene.paint.Color;
import tunnelers.Game.Chat.IChatParticipant;

/**
 *
 * @author Stepan
 */
public abstract class Player implements IChatParticipant{
	
	private final static Settings SETTINGS = Settings.getInstance();

	private final int playerID;
	private String name;
	private final Controls controls;
	private Color color;
	private Tank tank;

	public Player(int playerID, int colorID) {
		this(playerID, colorID, String.format("Unknown player %03d", playerID));
	}
	public Player(int playerID, int colorID, String name) {
		//public Player(String name) throws PlayerException{
		this.playerID = playerID;
		this.name = name;
		this.setColor(colorID);
		
		this.controls = new Controls();
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	public final void setColor(int colorId) {
		this.color = SETTINGS.getColor(this.color, colorId);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	public int getID() {
		return this.playerID;
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
		return controls;
	}

	@Override
	public String toString() {
		return String.format("[%2d] %16s (%s)", this.playerID, this.name, this.color);
	}
	
}
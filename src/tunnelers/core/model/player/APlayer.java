package tunnelers.core.model.player;

import tunnelers.core.chat.IChatParticipant;
import tunnelers.core.model.entities.Tank;

/**
 *
 * @author Stepan
 */
public abstract class APlayer implements IChatParticipant{

	private final int playerID;
	private String name;
	private final Controls controls;
	private int color;
	private Tank tank;

	public APlayer(int playerID, int colorID) {
		this(playerID, colorID, String.format("Unknown player %03d", playerID));
	}

	public APlayer(int playerID, int colorID, String name) {
		//public APlayer(String name) throws PlayerException{
		this.playerID = playerID;
		this.name = name;
		this.setColor(colorID);

		this.controls = new Controls();
	}

	
	@Override
	public int getColor() {
		return this.color;
	}

	public final void setColor(int colorId) {
		this.color = colorId;
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
		if (this.tank != null) {
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

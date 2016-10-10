package tunnelers.core.model.player;

import tunnelers.core.chat.IChatParticipant;
import tunnelers.core.io.AControls;
import tunnelers.core.model.entities.Tank;

/**
 *
 * @author Stepan
 */
public abstract class APlayer implements IChatParticipant{

	private final int playerID;
	private String name;
	private final AControls controls;
	private int color;
	private Tank tank;

	public APlayer(int playerID, int colorID, AControls controls) {
		this(playerID, colorID, controls, String.format("Unknown player %02d", playerID));
	}

	public APlayer(int playerID, int colorID, AControls controls, String name) {
		//public APlayer(String name) throws PlayerException{
		this.playerID = playerID;
		this.name = name;
		this.setColor(colorID);

		this.controls = controls;
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

	public AControls getControls() {
		return controls;
	}

	@Override
	public String toString() {
		return String.format("[%2d] %16s (%s)", this.playerID, this.name, this.color);
	}

}

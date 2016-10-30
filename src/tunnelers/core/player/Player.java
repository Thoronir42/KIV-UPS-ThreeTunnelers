package tunnelers.core.player;

import tunnelers.app.render.colors.Colorable;
import tunnelers.core.chat.IChatParticipant;
import tunnelers.core.model.entities.Tank;

/**
 *
 * @author Stepan
 */
public final class Player implements IChatParticipant, Colorable{

	private final int id;
	private String name;
	private final Controls controls;
	private int color;
	private Tank tank;

	public Player(int playerID, int colorID, Controls controls) {
		this(playerID, colorID, controls, String.format("Unknown player %02d", playerID));
	}

	public Player(int playerID, int colorID, Controls controls, String name) {
		//public Player(String name) throws PlayerException{
		this.id = playerID;
		this.name = name;
		this.setColor(colorID);

		this.controls = controls;
		this.controls.setPlayerID(this.id);
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
		return this.id;
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
		return String.format("[%2d] %16s (%s)", this.id, this.name, this.color);
	}

}

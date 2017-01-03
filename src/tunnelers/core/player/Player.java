package tunnelers.core.player;

import tunnelers.core.colors.IColorable;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.chat.IChatParticipant;
import tunnelers.core.model.entities.Tank;
import tunnelers.network.NetClient;

/**
 *
 * @author Stepan
 */
public final class Player implements IChatParticipant, IColorable{

	private final int id;
	
	private final NetClient client;
	private final Controls controls;
	private int color;
	private Tank tank;
	
	public Player(int playerID, int colorID, NetClient client, Controls controls) {
		this.id = playerID;
		this.color = colorID;
		this.client = client;

		this.controls = controls;
		this.controls.setPlayerID(this.id);
	}

	
	@Override
	public int getColor() {
		return this.color;
	}

	public void setColor(int colorId) {
		this.color = colorId;
	}

	@Override
	public String getName() {
		return this.client.getName(this);
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
		return String.format("[%2d] %16s (%s)", this.id, this.getName(), this.color);
	}

}

package tunnelers.core.player;

import tunnelers.core.colors.IColorable;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.chat.IChatParticipant;
import tunnelers.network.NetClient;

/**
 *
 * @author Stepan
 */
public final class Player implements IChatParticipant, IColorable{

	private final NetClient client;
	private final Controls controls;
	private int color;
	
	public Player(int colorID, NetClient client, Controls controls) {
		this.color = colorID;
		this.client = client;

		this.controls = controls;
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

	public Controls getControls() {
		return controls;
	}

	@Override
	public String toString() {
		return String.format("Player %16s (color=%02d)", this.getName(), this.color);
	}

}

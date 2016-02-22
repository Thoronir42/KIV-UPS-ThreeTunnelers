package tunnelers.Game.IO;

import java.util.Objects;

/**
 *
 * @author Stepan
 */
public class PlrInput {

	public final byte playerId;
	public final Input input;

	public PlrInput(byte player, Input i) {
		this.playerId = player;
		this.input = i;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + this.playerId;
		hash = 83 * hash + Objects.hashCode(this.input);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlrInput other = (PlrInput) obj;
		if (this.playerId != other.playerId) {
			return false;
		}
		return this.input == other.input;
	}

	public byte getPlayerId() {
		return playerId;
	}

	public Input getInput() {
		return input;
	}

	
	
}

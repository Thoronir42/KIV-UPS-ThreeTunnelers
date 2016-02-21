package tunnelers.Game.IO;

import java.util.Objects;

/**
 *
 * @author Stepan
 */
public class PlrInput {

	public final byte player;
	public final Input input;

	public PlrInput(byte player, Input i) {
		this.player = player;
		this.input = i;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + this.player;
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
		if (this.player != other.player) {
			return false;
		}
		return this.input == other.input;
	}

	

}

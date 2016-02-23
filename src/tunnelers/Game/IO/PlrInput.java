package tunnelers.Game.IO;

import java.util.Objects;

/**
 *
 * @author Stepan
 */
public class PlrInput {

	protected final byte controlSchemeID;
	protected final Input input;

	public PlrInput(byte controlSchemeID, Input i) {
		this.controlSchemeID = controlSchemeID;
		this.input = i;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + this.controlSchemeID;
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
		if (this.controlSchemeID != other.controlSchemeID) {
			return false;
		}
		return this.input == other.input;
	}

	public byte getControlSchemeId() {
		return controlSchemeID;
	}

	public Input getInput() {
		return input;
	}

	
	
}

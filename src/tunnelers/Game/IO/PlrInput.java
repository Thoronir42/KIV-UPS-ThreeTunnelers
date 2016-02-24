package tunnelers.Game.IO;

import java.util.Objects;

/**
 *
 * @author Stepan
 */
public class PlrInput {

	protected final AControlScheme controlScheme;
	protected final Input input;

	public PlrInput(AControlScheme controlScheme, Input i) {
		this.controlScheme = controlScheme;
		this.input = i;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + this.controlScheme.getID();
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
		if (this.controlScheme != other.controlScheme) {
			return false;
		}
		return this.input == other.input;
	}

	public AControlScheme getControlScheme() {
		return controlScheme;
	}

	public Input getInput() {
		return input;
	}

	
	
}

package tunnelers.Game.IO;

import tunnelers.core.io.AControls;
import tunnelers.core.io.InputAction;
import java.util.Objects;

/**
 *
 * @author Stepan
 */
public class ControlInput {

	protected final AControls controlScheme;
	protected final InputAction input;

	public ControlInput(AControls controlScheme, InputAction i) {
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
		final ControlInput other = (ControlInput) obj;
		if (this.controlScheme != other.controlScheme) {
			return false;
		}
		return this.input == other.input;
	}

	public AControls getControlScheme() {
		return controlScheme;
	}

	public InputAction getInput() {
		return input;
	}

	@Override
	public String toString() {
		return "ControlInput{" + "controlScheme=" + controlScheme.getID() + ", input=" + input + '}';
	}

	
	
}

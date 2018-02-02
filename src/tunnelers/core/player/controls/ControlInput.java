package tunnelers.core.player.controls;

public class ControlInput {

	private final Controls controlScheme;
	protected final InputAction input;

	public ControlInput(Controls controlScheme, InputAction i) {
		this.controlScheme = controlScheme;
		this.input = i;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + this.controlScheme.getID();
		hash = 83 * hash + this.input.intVal();
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
		return this.controlScheme == other.controlScheme && this.input == other.input;
	}

	public Controls getControlScheme() {
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

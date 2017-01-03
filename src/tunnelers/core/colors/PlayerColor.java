package tunnelers.core.colors;

/**
 *
 * @author Stepan
 */
public abstract class PlayerColor implements IColorable{

	protected final int value;
	protected boolean inUse;

	public PlayerColor(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}

	public boolean isInUse() {
		return this.inUse;
	}

	public void setInUse(boolean value) {
		this.inUse = value;
	}

	@Override
	public int getColor() {
		return this.intValue();
	}
}

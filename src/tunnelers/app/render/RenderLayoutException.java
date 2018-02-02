package tunnelers.app.render;

public class RenderLayoutException extends IllegalStateException {

	public RenderLayoutException(int playerAmount) {
		super("Couldn't create layout for " + playerAmount + " players");
	}
}

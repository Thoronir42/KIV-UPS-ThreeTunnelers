package tunnelers.Game.IO;

import javafx.scene.input.KeyCode;

/**
 *
 * @author Stepan
 */
public abstract class AControlScheme {

	private static int controlSchemesCreated = 0;

	protected final int schemeID;
	protected int playerID;

	public AControlScheme() {
		schemeID = controlSchemesCreated++;
	}

	public int getID() {
		return this.schemeID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int id) {
		this.playerID = id;
	}

	public abstract PlrInput getInput(KeyCode kc);
}

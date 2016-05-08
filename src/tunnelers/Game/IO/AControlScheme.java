package tunnelers.Game.IO;

import javafx.scene.input.KeyCode;

/**
 *
 * @author Stepan
 */
public abstract class AControlScheme {

	private static byte controlSchemesCreated = 0;

	protected final byte schemeID;
	protected int playerID;

	public AControlScheme() {
		schemeID = controlSchemesCreated++;
	}

	public byte getID() {
		return this.schemeID;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int id) {
		this.playerID = id;
	}

	public abstract ControlInput getInput(KeyCode kc);
}

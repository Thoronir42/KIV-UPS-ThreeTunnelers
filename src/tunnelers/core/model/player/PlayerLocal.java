package tunnelers.core.model.player;

import tunnelers.core.io.AControls;

/**
 *
 * @author Stepan
 */
public class PlayerLocal extends APlayer {

	public PlayerLocal(int playerID, int colorID, AControls controls) {
		super(playerID, colorID, controls);
	}

	public PlayerLocal(int playerID, int colorID, AControls controls, String name) {
		super(playerID, colorID, controls, name);
	}

	
}

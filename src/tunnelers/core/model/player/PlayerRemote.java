package tunnelers.core.model.player;

import tunnelers.core.io.InputAction;
import tunnelers.core.io.RemoteControlScheme;

/**
 *
 * @author Stepan
 */
public class PlayerRemote extends APlayer {

	public PlayerRemote(int playerID, int colorID) {
		super(playerID, colorID, new RemoteControlScheme((byte)playerID));
	}

	public PlayerRemote(int playerID, int colorID, String name) {
		super(playerID, colorID, new RemoteControlScheme((byte)playerID), name);
	}

	public void mockControls(long seed) {
		InputAction[] inputs = InputAction.values();
		RemoteControlScheme.RNG.setSeed(seed);
		for(InputAction ia : inputs){
			this.getControls().setControlState(ia, RemoteControlScheme.RNG.nextBoolean());
		}
	}

	

}

package tunnelers.Game.Frame;

import java.util.Random;
import tunnelers.Game.IO.InputAction;

/**
 *
 * @author Stepan
 */
public class PlayerRemote extends Player {

	public PlayerRemote(int playerID, int colorID) {
		super(playerID, colorID);
	}

	public PlayerRemote(int playerID, int colorID, String name) {
		super(playerID, colorID, name);
	}

	void mockControls() {
		InputAction[] inputs = InputAction.values();
		Random rand = new Random();
		for(InputAction ia : inputs){
			this.getControls().heldKeys.put(ia, rand.nextBoolean());
		}
	}

	

}

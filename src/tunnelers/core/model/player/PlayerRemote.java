package tunnelers.core.model.player;

import java.util.Random;
import tunnelers.Game.IO.InputAction;

/**
 *
 * @author Stepan
 */
public class PlayerRemote extends APlayer {

	private final Random rand;
	
	public PlayerRemote(int playerID, int colorID) {
		super(playerID, colorID);
		this.rand = new Random();
	}

	public PlayerRemote(int playerID, int colorID, String name) {
		super(playerID, colorID, name);
		this.rand = new Random();
	}

	public void mockControls(long seed) {
		InputAction[] inputs = InputAction.values();
		this.rand.setSeed(seed);
		for(InputAction ia : inputs){
			this.getControls().heldKeys.put(ia, rand.nextBoolean());
		}
	}

	

}

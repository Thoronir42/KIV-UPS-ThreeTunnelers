package tunnelers.core.gameRoom;

/**
 *
 * @author Skoro
 */
public class WarzoneRules {

	public int getTankMaxHP() {
		return 20;
	}

	public int getTankMaxEP() {
		return 250;
	}
	
	public int getProjectilesPerTank() {
		return 20;
	}
	
	public int getTankCannonCooldown(){
		return 5;
	}
	
	public int getCooldownRate(){
		return 1;
	}
}

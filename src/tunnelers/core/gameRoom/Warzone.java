package tunnelers.core.gameRoom;

import java.util.Collection;
import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Warzone {

	private final Map map;

	private final Tank[] tanks;
	private final Projectile[] projectiles;

	public Warzone(Tank[] tanks, Map map, int projectileCapacity) {
		this.tanks = tanks;
		this.map = map;

		this.projectiles = new Projectile[projectileCapacity];
	}

	public Projectile[] getProjectiles() {
		return projectiles;
	}

	public Map getMap() {
		return this.map;
	}

	public void setProjectile(int id, IntPoint location, Direction direction, Player player) {
		this.projectiles[id] = new Projectile(location, direction, player);
	}
	
	public void removeProjectile(int id){
		this.projectiles[id] = null;
	}

	public Tank[] getTanks() {
		return this.tanks;
	}
}

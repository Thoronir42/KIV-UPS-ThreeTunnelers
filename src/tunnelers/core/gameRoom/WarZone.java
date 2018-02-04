package tunnelers.core.gameRoom;

import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;

public class WarZone {

	private final WarZoneRules rules;
	private Map map;

	private final Tank[] tanks;
	private final Projectile[] projectiles;

	public WarZone(WarZoneRules rules, int tankCapacity) {
		this.rules = rules;
		this.tanks = new Tank[tankCapacity];
		this.projectiles = new Projectile[tankCapacity * rules.getProjectilesPerTank()];
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public WarZoneRules getRules() {
		return this.rules;
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

	public void removeProjectile(int id) {
		this.projectiles[id] = null;
	}

	public Tank getTank(int roomId) {
		if (roomId < 0 || roomId >= tanks.length) {
			throw new IndexNotInRangeException(0, tanks.length - 1, roomId);
		}

		return this.tanks[roomId];

	}

	public Tank[] getTanks() {
		return this.tanks;
	}

	public void initTank(int playerRID, Player p, IntPoint location) {
		this.tanks[playerRID] = new Tank(p, location)
				.setHitPoints(rules.getTankMaxHP())
				.setEnergy(rules.getTankMaxEP());

	}
}

package tunnelers.core.engine.stage;

import temp.Mock;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.Warzone;
import tunnelers.core.player.controls.Controls;
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
public class WarzoneStage extends AEngineStage {

	private static final int COOLDOWN_RATE = 1;

	private final GameRoom gameRoom;
	private final Warzone warzone;

	public WarzoneStage(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
		this.warzone = gameRoom.getWarzone();
	}

	@Override
	public void update(long tick) {
		if (tick % 3 == 0) {
			Player[] players = this.gameRoom.getPlayers();
			Tank[] tanks = this.warzone.getTanks();

			for (int i = 0; i < players.length; i++) {
				Player p = players[i];
				Tank t = tanks[i];
				if (p == null) {
					continue;
				}

				this.updateTank(t, p.getControls());
			}

		}
		if (tick % 15 == 0) {
			Mock.controls(tick);
		}

		this.updateProjectiles(this.warzone.getProjectiles(), tick);
	}

	private void updateTank(Tank tank, Controls c) {
		tank.cooldown(COOLDOWN_RATE);
		if (true && c.isShooting()) { // TODO: omezeni poctu strel
			IntPoint location = tank.tryShoot();
			if (location != null) {
				int projectilePosition = findFreeProjectileSlot(this.warzone.getProjectiles());
				if (projectilePosition >= 0) {
					warzone.setProjectile(projectilePosition, location, tank.getDirection(), tank.getPlayer());
				}
			}
		}

		moveTank(tank, c.getDirection());
	}

	private int findFreeProjectileSlot(Projectile[] projectiles) {
		for (int i = 0; i < projectiles.length; i++) {
			if (projectiles[i] == null) {
				return i;
			}
		}

		return -1;
	}

	protected IntPoint moveTank(Tank tank, Direction d) {
		Map map = this.warzone.getMap();

		if (d == null || d == Direction.Undefined) {
			return null;
		}
		IntPoint plr_loc = tank.getLocation();
		int newX = plr_loc.getX() + d.getX(),
				newY = plr_loc.getY() + d.getY();

		if ((newY - tank.getHeight() / 2 > 0)
				&& (newX - tank.getWidth() / 2 > 0)
				&& (newX + tank.getWidth() / 2 < map.getBlockWidth())
				&& (newY + tank.getHeight() / 2 < map.getBlockHeight())) {
			tank.setLocation(new IntPoint(newX, newY));
			tank.setDirection(d);
		}

		return tank.getLocation();
	}

	private void updateProjectiles(Projectile[] projectiles, long tick) {
		Map map = this.warzone.getMap();

		for (int i = 0; i < projectiles.length; i++) {
			Projectile p = projectiles[i];
			if (p == null) {
				continue;
			}

			IntPoint newLocation = p.getLocation().copy();
			newLocation.add(p.getDirection().asPoint());

			if (newLocation.getX() < 0 || newLocation.getX() > map.getBlockWidth()
					|| newLocation.getY() < 0 || newLocation.getY() > map.getBlockHeight()) {
				projectiles[i] = null;
				continue;
			}

			p.setLocation(newLocation);
		}
	}

}

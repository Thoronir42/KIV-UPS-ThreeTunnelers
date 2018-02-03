package tunnelers.core.engine.stage;

import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.WarZone;
import tunnelers.core.gameRoom.WarZoneRules;
import tunnelers.core.model.entities.*;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;

public class WarZoneStage extends AEngineStage {

	private final GameRoom gameRoom;
	private final WarZone warZone;

	public WarZoneStage(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
		this.warZone = gameRoom.getWarZone();
	}

	@Override
	public void update(long tick) {
		if (tick % 3 == 0) {
			Tank[] tanks = this.warZone.getTanks();

			for (int i = 0; i < tanks.length; i++) {
				Tank t = tanks[i];
				if (t == null) {
					continue;
				}
				Player p = t.getPlayer();
				if (p == null) {
					System.err.println("No player assigned to tank " + i);
					continue;
				}
				if (t.getStatus() != Tank.Status.Operative) {
					if (t.getEnergy() > 0) {
						t.setEnergy(t.getEnergy() * 5 / 6);
					}
					continue;
				}

				this.updateTank(t, p.getControls());
			}

		}

		this.updateProjectiles(this.warZone.getProjectiles(), tick);
	}

	private void updateTank(Tank tank, Controls c) {
		/*boolean readyToShoot = tank.cooldown(warZoneRules.getCooldownRate());
		if (true && c.get(InputAction.actShoot)) { // TODO: omezeni poctu strel
			if (readyToShoot) {
				int projectilePosition = findFreeProjectileSlot(this.warZone.getProjectiles());
				if (projectilePosition >= 0) {
					warZone.setProjectile(projectilePosition, tank.getLocation(), tank.getDirection(), tank.getPlayer());
				}
				tank.setCoolDown(warZoneRules.getTankCannonCooldown());
			}
		}*/

		moveTank(tank, getDirection(c));
	}

	private Direction getDirection(Controls c) {
		int x = getDir(c, InputAction.movLeft, InputAction.movRight),
				y = getDir(c, InputAction.movUp, InputAction.movDown);
		return Direction.getDirection(x, y);
	}

	private int getDir(Controls c, InputAction sub, InputAction add) {
		if (c.get(sub) && !c.get(add)) {
			return -1;
		}
		if (c.get(add) && !c.get(sub)) {
			return 1;
		}
		return 0;
	}

	/*private int findFreeProjectileSlot(Projectile[] projectiles) {
		for (int i = 0; i < projectiles.length; i++) {
			if (projectiles[i] == null) {
				return i;
			}
		}

		return -1;
	}*/

	private boolean locationOccupable(Map map, int newX, int newY, Shape body, Shape belt) {
		IntPoint min = belt.getMin();
		IntPoint max = belt.getMax();
		for (int sy = min.getY(); sy <= max.getY(); sy++) {
			for (int sx = min.getX(); sx <= max.getX(); sx++) {
				if (!body.isPixelSolidRelative(sx, sy) || !belt.isPixelSolidRelative(sx, sy)) {
					continue;
				}
				Block b = map.getBlock(newX + sx, newY + sy);
				if (b.isObstacle()) {
					return false;
				}
			}
		}

		return true;
	}

	private IntPoint moveTank(Tank tank, Direction d) {
		Map map = this.warZone.getMap();

		if (d == null || d == Direction.Undefined) {
			return null;
		}

		Shape shapeBody = ShapeFactory.get(d, ShapeFactory.Type.TankBody);
		Shape shapeBelt = ShapeFactory.get(d, ShapeFactory.Type.TankBelt);

		IntPoint plr_loc = tank.getLocation();
		int newX = plr_loc.getX() + d.getX(),
				newY = plr_loc.getY() + d.getY();

		if (locationOccupable(map, newX, newY, shapeBody, shapeBelt)) {
			tank.setLocation(newX, newY);
			tank.setDirection(d);
		}

		return tank.getLocation();
	}

	private void updateProjectiles(Projectile[] projectiles, long tick) {
		Map map = this.warZone.getMap();

		for (int i = 0; i < projectiles.length; i++) {
			Projectile p = projectiles[i];
			if (p == null) {
				continue;
			}

			IntPoint newLocation = p.getLocation().copy();
			newLocation.add(p.getDirection().asPoint());

			Block b = map.getBlock(newLocation.getX(), newLocation.getY());
			switch (b) {
				case Dirt:
					map.setBlock(newLocation.getX(), newLocation.getY(), Block.Empty);
				case BaseWall:
				case Rock:
					projectiles[i] = null;
					continue;
			}

			if (newLocation.getX() < 0 || newLocation.getX() > map.getBlockWidth()
					|| newLocation.getY() < 0 || newLocation.getY() > map.getBlockHeight()) {
				projectiles[i] = null;
				continue;
			}

			p.setLocation(newLocation);
		}
	}

}

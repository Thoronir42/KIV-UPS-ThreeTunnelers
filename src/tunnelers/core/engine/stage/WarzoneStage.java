package tunnelers.core.engine.stage;

import generic.RNG;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.Warzone;
import tunnelers.core.gameRoom.WarzoneRules;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Shape;
import tunnelers.core.model.entities.ShapeFactory;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.InputAction;

/**
 *
 * @author Stepan
 */
public class WarzoneStage extends AEngineStage {

	private final GameRoom gameRoom;
	private final Warzone warzone;
	private final WarzoneRules warzoneRules;

	public WarzoneStage(GameRoom gameRoom) {
		this.gameRoom = gameRoom;
		this.warzone = gameRoom.getWarzone();
		this.warzoneRules = warzone.getRules();
	}

	@Override
	public void update(long tick) {
		if (tick % 3 == 0) {
			Player[] players = this.gameRoom.getPlayers();
			Tank[] tanks = this.warzone.getTanks();

			for (int i = 0; i < players.length; i++) {
				Player p = players[i];
				Tank t = tanks[i];
				if (t == null) {
					continue;
				}
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

		this.updateProjectiles(this.warzone.getProjectiles(), tick);
	}

	private void updateTank(Tank tank, Controls c) {
//		boolean readyToShoot = tank.cooldown(warzoneRules.getCooldownRate());
//		if (true && c.get(InputAction.actShoot)) { // TODO: omezeni poctu strel
//			if (readyToShoot) {
//				int projectilePosition = findFreeProjectileSlot(this.warzone.getProjectiles());
//				if (projectilePosition >= 0) {
//					warzone.setProjectile(projectilePosition, tank.getLocation(), tank.getDirection(), tank.getPlayer());
//				}
//				tank.setCooldown(warzoneRules.getTankCannonCooldown());
//			}
//		}

		moveTank(tank, getDirection(c));
	}

	public Direction getDirection(Controls c) {
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

	private int findFreeProjectileSlot(Projectile[] projectiles) {
		for (int i = 0; i < projectiles.length; i++) {
			if (projectiles[i] == null) {
				return i;
			}
		}

		return -1;
	}

	protected boolean locationOccupable(int newX, int newY, Shape body, Shape belt) {
		IntPoint min = belt.getMin();
		IntPoint max = belt.getMax();
		for (int sy = min.getY(); sy <= max.getY(); sy++) {
			for (int sx = min.getX(); sx <= max.getX(); sx++) {
				if (!body.isPixelSolidRelative(sx, sy) || !belt.isPixelSolidRelative(sx, sy)) {
					continue;
				}
				Block b = this.warzone.getMap().getBlock(newX + sx, newY + sy);
				if (b.isObstacle()) {
					return false;
				}
			}
		}

		return true;
	}

	protected IntPoint moveTank(Tank tank, Direction d) {
		Map map = this.warzone.getMap();

		if (d == null || d == Direction.Undefined) {
			return null;
		}

		Shape shapeBody = ShapeFactory.get(d, ShapeFactory.Type.TankBody);
		Shape shapeBelt = ShapeFactory.get(d, ShapeFactory.Type.TankBelt);

		IntPoint plr_loc = tank.getLocation();
		int newX = plr_loc.getX() + d.getX(),
				newY = plr_loc.getY() + d.getY();

		if (locationOccupable(newX, newY, shapeBody, shapeBelt)) {
			tank.setLocation(newX, newY);
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

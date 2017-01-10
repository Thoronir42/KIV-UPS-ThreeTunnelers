package tunnelers.core.engine.stage;

import java.util.Collection;
import java.util.Iterator;
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
			for(Player p : players){
				this.updateTank(p.getTank(), p.getControls());
			};

		}
		if(tick % 15 == 0){
			Mock.controls(tick);
		}

		this.updateProjectiles(this.warzone.getProjectiles(), tick);
	}

	private void updateTank(Tank tank, Controls c) {
		tank.cooldown();
		if (true && c.isShooting()) { // TODO: omezeni poctu strel
			IntPoint location = tank.tryShoot();
			if (location != null) {
				warzone.addProjectile(location, tank.getDirection(), tank.getPlayer());
			}
		}

		moveTank(tank, c.getDirection());
	}

	protected IntPoint moveTank(Tank tank, Direction d) {
		Map map = this.gameRoom.getWarzone().getMap();

		if (d == null) {
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

	private void updateProjectiles(Collection<Projectile> projectiles, long tick) {
		Map map = this.gameRoom.getWarzone().getMap();
		
		for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
			Projectile p = it.next();
			
			IntPoint newLocation = p.getLocation().copy();
			newLocation.add(p.getDirection().asPoint());

			if (newLocation.getX() < 0 || newLocation.getX() > map.getBlockWidth()
					|| newLocation.getY() < 0 || newLocation.getY() > map.getBlockHeight()) {
				it.remove();
				continue;
			}

			p.setLocation(newLocation);
		}
	}

}

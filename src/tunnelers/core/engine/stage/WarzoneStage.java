package tunnelers.core.engine.stage;

import java.util.Collection;
import java.util.Iterator;
import javafx.geometry.Point2D;
import tunnelers.core.GameContainer;
import tunnelers.core.Warzone;
import tunnelers.core.io.AControls;
import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.map.Map;
import tunnelers.core.model.player.PlayerRemote;

/**
 *
 * @author Stepan
 */
public class WarzoneStage extends AEngineStage {

	private final GameContainer container;
	private final Warzone warzone;

	public WarzoneStage(GameContainer container) {
		this.container = container;
		this.warzone = container.getWarzone();

	}

	@Override
	public void update(long tick) {
		if (tick % 3 == 0) {
			this.container.getPlayers().forEach((p) -> {
				if (p instanceof PlayerRemote && tick % 15 == 0) {
					((PlayerRemote) p).mockControls(tick);
				}
				this.updateTank(p.getTank(), p.getControls());
			});

		}

		this.updateProjectiles(this.warzone.getProjectiles(), tick);
	}

	private void updateTank(Tank tank, AControls c) {
		tank.update();
		if (true && c.isShooting()) { // TODO: omezeni poctu strel
			Point2D location = tank.tryShoot();
			if (location != null) {
				warzone.addProjectile(location, tank.getDirection(), tank.getPlayer());
			}
		}

		moveTank(tank, c.getDirection());
	}

	protected Point2D moveTank(Tank tank, Direction d) {
		Map map = this.container.getWarzone().getMap();

		if (d == null) {
			return null;
		}
		Point2D plr_loc = tank.getLocation();
		double newX = plr_loc.getX() + d.getX(),
				newY = plr_loc.getY() + d.getY();

		if ((newY - tank.getHeight() / 2 > 0)
				&& (newX - tank.getWidth() / 2 > 0)
				&& (newX + tank.getWidth() / 2 < map.getWidth())
				&& (newY + tank.getHeight() / 2 < map.getHeight())) {
			tank.setLocation(new Point2D(newX, newY));
			tank.setDirection(d);
		}

		return tank.getLocation();
	}

	private void updateProjectiles(Collection<Projectile> projectiles, long tick) {
		Map map = this.container.getWarzone().getMap();
		
		for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
			Projectile p = it.next();
			Point2D newLocation = p.getLocation().add(p.getDirection().asPoint());

			if (newLocation.getX() < 0 || newLocation.getX() > map.getWidth()
					|| newLocation.getY() < 0 || newLocation.getY() > map.getHeight()) {
				it.remove();
				continue;
			}

			p.setLocation(newLocation);
		}
	}

}

package tunnelers.core.engine.stage;

import java.util.Collection;
import javafx.geometry.Point2D;
import tunnelers.core.GameContainer;
import tunnelers.core.Warzone;
import tunnelers.core.io.AControls;
import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.player.APlayer;
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

		Warzone warzone = this.container.getWarzone();
		if (warzone != null) {
			warzone.update();
		}
		if (tick % 4 == 0) {
			updatePlayers(this.container.getPlayers(), tick);
		}
		
		this.updateProjectiles(this.warzone.getProjectiles(), tick);
	}

	private void updatePlayers(Collection<APlayer> players, long tick) {
		for (APlayer p : players) {
			if (p instanceof PlayerRemote && tick % 15 == 0) {
				((PlayerRemote) p).mockControls(tick);
			}
			Tank tank = p.getTank();
			AControls c = p.getControls();

			tank.update();
			if (true && c.isShooting()) { // TODO: omezeni poctu strel
				tankShoot(tank);
			}

			updateTank(tank, c.getDirection());
		}
	}

	protected void tankShoot(Tank tank) {
		Point2D location = tank.tryShoot();
		if (location == null) {
			return; 
		}
		
		warzone.addProjectile(location, tank.getDirection(), tank.getPlayer());
	}

	protected Point2D updateTank(Tank tank, Direction d) {
		if (d == null) {
			return null;
		}
		Point2D plr_loc = tank.getLocation();
		double newX = plr_loc.getX() + d.getX(),
				newY = plr_loc.getY() + d.getY();

		if ((newY - tank.getHeight() / 2 > 0)
				&& (newX - tank.getWidth() / 2 > 0)
				&& (newX + tank.getWidth() / 2 < this.container.getMap().getWidth())
				&& (newY + tank.getHeight() / 2 < this.container.getMap().getHeight())) {
			tank.setLocation(new Point2D(newX, newY));
			tank.setDirection(d);
		}

		return tank.getLocation();
	}

	private void updateProjectiles(Collection<Projectile> projectiles, long tick) {
		for(Projectile p : projectiles){
			p.getLocation().add(p.getDirection().getDirection());
		}
	}

}

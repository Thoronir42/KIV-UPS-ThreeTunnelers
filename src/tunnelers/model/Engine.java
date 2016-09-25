package tunnelers.model;

import tunnelers.model.entities.Direction;
import tunnelers.model.entities.Tank;
import tunnelers.model.player.Controls;
import tunnelers.model.player.APlayer;
import tunnelers.model.player.PlayerRemote;
import java.util.Collection;
import javafx.geometry.Point2D;

/**
 *
 * @author Stepan
 */
public final class Engine {
	
	private final GameContainer container;
	
	public Engine(GameContainer container){
		this.container = container;
	}

	public GameContainer getContainer() {
		return container;
	}
	
	public APlayer getPlayer(int n){
		return this.container.getPlayer(n);
	}
	
	
	public void update(long tick){
		Warzone warzone = this.container.getWarzone();
		if(warzone != null){
			warzone.update();
		}
		updatePlayers(this.container.getPlayers(), tick);
	}
	
	private void updatePlayers(Collection<APlayer> players, long tick) {
		for (APlayer p : players) {
			if(p instanceof PlayerRemote && tick % 15 == 0){
				((PlayerRemote)p).mockControls(tick);
			}
			Tank tank = p.getTank();
			Controls c = p.getControls();
			
			tank.update();
			tankShoot(tank, true && c.isShooting());		// TODO: omezeni poctu strel
			updateTank(tank, c.getDirection());	
		}
	}

	protected void tankShoot(Tank tank, boolean tryShoot){
		if(tryShoot){
			System.out.print("Click... ");
			if(tank.tryShoot() != null){
				System.out.print("BANG!!!");
			}
			System.out.println("");
		}
	}
	
	protected Point2D updateTank(Tank tank, Direction d) {if (d == null) {
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
}

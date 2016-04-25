package tunnelers.Game.Frame;

import java.util.ArrayList;
import javafx.geometry.Point2D;

/**
 *
 * @author Stepan
 */
public final class Engine {
	
	private final Container container;
	
	public Engine(Container container){
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}
	
	public Player getPlayer(int n){
		return this.container.getPlayer(n);
	}
	
	
	public void update(long tick){
		updatePlayers(this.container.getPlayers(), tick);
		updateProjectiles(this.container.getProjectiles());
	}
	
	private void updatePlayers(Player[] players, long tick) {
		for (Player p : players) {
			if(p instanceof PlayerRemote && tick % 15 == 0){
				((PlayerRemote)p).mockControls();
			}
			Tank tank = p.getTank();
			Controls c = p.getControls();
			
			tank.update();
			tankShoot(tank, true && c.isShooting());		// TODO: omezeni poctu strel
			updateTank(tank, c.getDirection());	
			
			updateProjectiles(container.getProjectiles());
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
	
	private void updateProjectiles(ArrayList<Projectile> projectiles) {
		for(Projectile p : projectiles){
			break;
		}
	}
	
}

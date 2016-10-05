package tunnelers.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import tunnelers.model.entities.Projectile;
import tunnelers.model.map.Zone;
import tunnelers.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class Warzone {

	private final Zone zone;
	
	private final List<APlayer> players;
	private final List<Projectile> projectiles;
	
	public Warzone(List<APlayer> players, Zone zone){
		this.players = players;
		this.zone = zone;
		
		this.projectiles = new ArrayList<>();
	}
	
	public Collection<Projectile> getProjectiles() {
		return projectiles;
	}

	public void update() {
		this.updateProjectiles();
	}
	
	private void updateProjectiles() {
		for(Projectile p : this.projectiles){
			break;
		}
	}

	Zone getMap() {
		return this.zone;
	}
}

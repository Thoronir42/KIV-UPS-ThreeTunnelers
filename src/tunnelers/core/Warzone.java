package tunnelers.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.map.Map;
import tunnelers.core.model.player.APlayer;

/**
 *
 * @author Stepan
 */
public class Warzone {

	private final Map map;
	
	private final List<APlayer> players;
	private final List<Projectile> projectiles;
	
	public Warzone(List<APlayer> players, Map map){
		this.players = players;
		this.map = map;
		
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

	public Map getMap() {
		return this.map;
	}
}

package tunnelers.core.gameRoom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.geometry.Point2D;
import tunnelers.core.model.entities.Direction;
import tunnelers.core.model.entities.Projectile;
import tunnelers.core.model.entities.Tank;
import tunnelers.core.model.map.Map;
import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Warzone {

	private final Map map;

	private final Tank[] tanks;
	private final List<Projectile> projectiles;

	public Warzone(Tank[] tanks, Map map) {
		this.tanks = tanks;
		this.map = map;

		this.projectiles = new ArrayList<>();
	}

	public Collection<Projectile> getProjectiles() {
		return projectiles;
	}

	public Map getMap() {
		return this.map;
	}

	public void addProjectile(Point2D location, Direction direction, Player player) {
		this.projectiles.add(new Projectile(location, direction, player));
	}
}

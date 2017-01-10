package tunnelers.core.model.entities;

import tunnelers.core.player.Player;
import javafx.geometry.Dimension2D;

/**
 *
 * @author Stepan
 */
public class Projectile extends GameEntity {

	private static final Dimension2D SHOT_HORIZONTAL = new Dimension2D(3, 1),
			SHOT_DIAGONAL = new Dimension2D(3, 3);

	public Projectile(IntPoint location, Direction direction, Player player) {
		super(direction, location, player);
	}

	@Override
	public Dimension2D getSize() {
		return this.direction.isDiagonal() ? SHOT_DIAGONAL : SHOT_HORIZONTAL;
	}
}

package tunnelers.core.model.entities;

import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Projectile extends GameEntity {

	private static final IntDimension SHOT_HORIZONTAL = new IntDimension(3, 1),
			SHOT_DIAGONAL = new IntDimension(3, 3);

	public Projectile(IntPoint location, Direction direction, Player player) {
		super(direction, location, player);
	}

	@Override
	public IntDimension getSize() {
		return this.direction.isDiagonal() ? SHOT_DIAGONAL : SHOT_HORIZONTAL;
	}
}

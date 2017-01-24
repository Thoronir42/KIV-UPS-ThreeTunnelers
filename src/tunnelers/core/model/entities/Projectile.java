package tunnelers.core.model.entities;

import tunnelers.core.player.Player;

/**
 *
 * @author Stepan
 */
public class Projectile extends GameEntity {

	public Projectile(IntPoint location, Direction direction, Player player) {
		super(location, direction, player);
	}

	@Override
	public IntDimension getSize() {
		return ShapeFactory.get(direction, ShapeFactory.Type.Projectile).getSize();
	}
}

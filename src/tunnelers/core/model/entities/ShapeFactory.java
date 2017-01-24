package tunnelers.core.model.entities;

public class ShapeFactory {

	public static final Shape TANK_BODY_UPRIGHT;
	public static final Shape TANK_BODY_DIAGONAL;

	public static final Shape TANK_BELT_NORTH;
	public static final Shape TANK_BELT_NORTH_EAST;
	public static final Shape TANK_BELT_EAST;
	public static final Shape TANK_BELT_SOUTH_EAST;

	public static final Shape PROJECTILE_NORTH;
	public static final Shape PROJECTILE_NORTH_EAST;
	public static final Shape PROJECTILE_EAST;
	public static final Shape PROJECTILE_SOUTH_EAST;

	static {
		TANK_BODY_UPRIGHT = getTankBodyUpright();
		TANK_BODY_DIAGONAL = getTankBodyDiagonal();

		TANK_BELT_NORTH = getTankBeltNorth();
		TANK_BELT_NORTH_EAST = getTankBeltNorthEast();
		TANK_BELT_EAST = rotateClockwise(TANK_BELT_NORTH);
		TANK_BELT_SOUTH_EAST = rotateClockwise(TANK_BELT_NORTH_EAST);

		PROJECTILE_NORTH = new Shape(1, 3, new boolean[]{true, true, true});
		PROJECTILE_EAST = new Shape(3, 1, new boolean[]{true, true, true});
		PROJECTILE_NORTH_EAST = getProjectileNorthEast();
		PROJECTILE_SOUTH_EAST = rotateClockwise(PROJECTILE_NORTH_EAST);
	}

	private ShapeFactory() {
	}

	public static Shape get(Direction direction, Type type) {
		switch (type) {
			case TankBody:
				return getBodyShape(direction);
			case TankBelt:
				return getBeltShape(direction);
			case Projectile:
				return getProjectileShape(direction);
			default:
				return null;
		}
	}

	private static Shape getBodyShape(Direction direction) {
		switch (direction) {
			case North:
			case East:
			case South:
			case West:
				return TANK_BODY_UPRIGHT;
			case NorthEast:
			case SouthEast:
			case SouthWest:
			case NorthWest:
				return TANK_BODY_DIAGONAL;
		}

		return null;
	}

	private static Shape getBeltShape(Direction direction) {
		switch (direction) {
			case North:
			case South:
				return TANK_BELT_NORTH;
			case East:
			case West:
				return TANK_BELT_EAST;
			case NorthEast:
			case SouthWest:
				return TANK_BELT_NORTH_EAST;
			case SouthEast:
			case NorthWest:
				return TANK_BELT_SOUTH_EAST;
		}
		return null;
	}

	private static Shape getProjectileShape(Direction direction) {
		switch (direction) {
			case North:
			case South:
				return PROJECTILE_NORTH;
			case East:
			case West:
				return PROJECTILE_EAST;
			case NorthEast:
			case SouthWest:
				return PROJECTILE_NORTH_EAST;
			case SouthEast:
			case NorthWest:
				return PROJECTILE_SOUTH_EAST;
		}
		return null;
	}

	private static Shape rotateClockwise(Shape src) {
		int newWidth = src.getHeight();
		int newHeight = src.getWidth();
		boolean[] pixels = new boolean[newWidth * newHeight];
		
		for (int j = 0; j < src.getHeight(); j++) {
        for (int i = 0; i < src.getWidth(); i++) {
            int dest_offset = (i * newWidth) + (newWidth - 1 - j);
            pixels[dest_offset] = src.isPixelSolid(i, j);
        }
    }

		return new Shape(newWidth, newHeight, pixels);
	}

	private static Shape getTankBodyUpright() {
		return new Shape(3, 5, new boolean[]{
			true, true, true,
			true, true, true,
			true, true, true,
			true, true, true,
			false, false, false
		});
	}

	private static Shape getTankBodyDiagonal() {
		return new Shape(5, 5, new boolean[]{
			false, false, true, false, false,
			false, true, true, true, false,
			true, true, true, true, true,
			false, true, true, true, false,
			false, false, true, false, false
		});
	}

	private static Shape getTankBeltNorth() {
		return new Shape(5, 7, new boolean[]{
			false, false, false, false, false,
			true, false, false, false, true,
			true, false, false, false, true,
			true, false, false, false, true,
			true, false, false, false, true,
			true, false, false, false, true,
			true, false, false, false, true
		});
	}

	private static Shape getTankBeltNorthEast() {
		return new Shape(7, 7, new boolean[]{
			false, false, false, true, false, false, false,
			false, false, true, false, false, false, false,
			false, true, false, false, false, false, false,
			true, false, false, false, false, false, true,
			false, false, false, false, false, true, false,
			false, false, false, false, true, false, false,
			false, false, false, true, false, false, false
		});
	}

	private static Shape getProjectileNorthEast() {
		return new Shape(3, 3, new boolean[]{
			false, false, true,
			false, true, false,
			true, false, false
		});
	}

	public static enum Type {
		TankBody, TankBelt, Projectile
	}
}

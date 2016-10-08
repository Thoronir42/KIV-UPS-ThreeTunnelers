package tunnelers.core.model.map;

/**
 *
 * @author Stepan
 */
class ChunkException extends ArrayIndexOutOfBoundsException {

	public ChunkException(int x, int y, int maxX, int maxY) {
		super(String.format("Invalid chunk position:[%dx%d] Max position (%dx%d) exceeded", x, y, maxX, maxY));
	}
}

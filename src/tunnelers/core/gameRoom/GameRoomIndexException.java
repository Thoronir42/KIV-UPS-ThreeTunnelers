package tunnelers.core.gameRoom;

/**
 *
 * @author Skoro
 */
public class GameRoomIndexException extends IndexOutOfBoundsException {
	public GameRoomIndexException(int min, int max, int given){
		super(String.format("Index must be in range of <%d, %d>, %d given", min, max, given));
	}
}

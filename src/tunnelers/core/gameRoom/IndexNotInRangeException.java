package tunnelers.core.gameRoom;

/**
 *
 * @author Skoro
 */
public class IndexNotInRangeException extends IndexOutOfBoundsException {
	public IndexNotInRangeException(int min, int max, int given){
		super(String.format("Index must be in range of <%d, %d>, %d given", min, max, given));
	}
}

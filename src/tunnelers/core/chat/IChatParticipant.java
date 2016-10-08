package tunnelers.core.chat;

/**
 *
 * @author Stepan
 */
public interface IChatParticipant {

	public static int SYSTEM_ID = -1;
	public String getName();
	public int getColor();
}

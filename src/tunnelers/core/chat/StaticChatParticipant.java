package tunnelers.core.chat;

/**
 *
 * @author Skoro
 */
public class StaticChatParticipant implements IChatParticipant {

	private final int color;
	private final String name;

	public StaticChatParticipant(String name, int color) {
		this.name = name;
		this.color = color;
	}
	
	@Override
	public int getColor() {
		return color;
	}

	@Override
	public String getName() {
		return name;
	}

}

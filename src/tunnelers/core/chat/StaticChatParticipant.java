package tunnelers.core.chat;

public class StaticChatParticipant implements IChatParticipant {

	private final int color;
	private final String name;

	StaticChatParticipant(String name, int color) {
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

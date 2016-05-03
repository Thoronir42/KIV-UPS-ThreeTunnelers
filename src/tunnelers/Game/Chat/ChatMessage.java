package tunnelers.Game.Chat;


import javafx.scene.paint.Color;



public class ChatMessage {

	private final IChatParticipant participant;
	private final String text;

	public ChatMessage(IChatParticipant participant, String message) {
		this.participant = participant;
		this.text = message;
	}

	@Override
	public String toString() {
		return String.format("%s : %s", participant.getName(), text);
	}

	public Color getColor() {
		return this.participant.getColor();
	}
}

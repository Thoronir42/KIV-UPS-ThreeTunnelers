package tunnelers.core.chat;

import tunnelers.core.chat.IChatParticipant;

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
	
	public String getName(){
		return this.participant.getName();
	}
	
	public String getText(){
		return this.text;
	}
	
	public String getHexColor(){
		return this.participant.getHexColor();
	}
}

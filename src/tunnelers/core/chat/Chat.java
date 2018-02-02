package tunnelers.core.chat;

import generic.CyclicArray;

import java.util.Iterator;

public class Chat {

	static {
		PARTICIPANT_SERVER = new StaticChatParticipant("SRV", IChatParticipant.SYSTEM_ID);
		PARTICIPANT_ERROR = new StaticChatParticipant("whoami", IChatParticipant.SYSTEM_ID);
	}

	private static final IChatParticipant PARTICIPANT_SERVER, PARTICIPANT_ERROR;

	public static IChatParticipant server() {
		return PARTICIPANT_SERVER;
	}

	public static IChatParticipant error() {
		return PARTICIPANT_ERROR;
	}

	private final CyclicArray<ChatMessage> messages;

	public Chat(int messageCapacity) {
		messages = new CyclicArray<>(ChatMessage.class, messageCapacity);
	}

	public void addMessage(IChatParticipant p, String text) {
		ChatMessage message = new ChatMessage(p.getName(), p.getColor(), text);
		addMessage(message);
	}

	public void addMessage(ChatMessage message) {
		this.messages.add(message);
	}

	public Iterator<ChatMessage> iterator() {
		return messages.iterator();
	}
}

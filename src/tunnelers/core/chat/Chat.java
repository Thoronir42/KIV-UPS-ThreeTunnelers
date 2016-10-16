package tunnelers.core.chat;

import generic.CyclicArray;
import java.util.Iterator;

/**
 *
 * @author Stepan
 */
public class Chat {

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

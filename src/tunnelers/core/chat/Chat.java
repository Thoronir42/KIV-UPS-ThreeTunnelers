package tunnelers.core.chat;

import generic.CyclicArray;
import java.util.Iterator;
import tunnelers.app.render.colors.AColorScheme;

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

	public String getHtml(AColorScheme colorScheme) {
		StringBuilder sb = new StringBuilder();

		Iterator<ChatMessage> it = this.messages.iterator();
		while (it.hasNext()) {
			ChatMessage msg = it.next();
			String rowHtml = String.format("<span><b style=\"color:#%s;\">%s</b>: %s</span><br/>", 
					colorScheme.getPlayerColor(msg.getColor()), msg.getName(), msg.getText());
			sb.insert(0, rowHtml);
		}

		return sb.toString();
	}
}

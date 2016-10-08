package tunnelers.core.chat;

import tunnelers.core.chat.IChatParticipant;
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
		ChatMessage message = new ChatMessage(p, text);
		addMessage(message);
	}

	public void addMessage(ChatMessage message) {
		this.messages.add(message);
	}
	
	public String getHtml(){
		StringBuilder sb = new StringBuilder();
		
		Iterator<ChatMessage> it = this.messages.iterator();
		while (it.hasNext()) {
			ChatMessage msg = it.next();
			String rowHtml = String.format("<span><b style=\"color:#%s;\">%s</b>: %s</span><br/>", msg.getHexColor(), msg.getName(), msg.getText());
			sb.insert(0, rowHtml);
		}
		
		return sb.toString();
	}
}
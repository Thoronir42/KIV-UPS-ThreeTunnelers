package tunnelers.Game.Chat;

import generic.CyclicArray;
import java.util.Iterator;

/**
 *
 * @author Stepan
 */
public class Chat {

	private final int MAX_MESSAGES = 12;

	private final CyclicArray<ChatMessage> messages;
	
	private final IChatParticipant localParticipant;

	public Chat(IChatParticipant localParticipant) {
		messages = new CyclicArray<>(ChatMessage.class, MAX_MESSAGES);
		this.localParticipant = localParticipant;
	}
	
	public void addMessage(String text) {
		this.addMessage(localParticipant, text);
	}

	public void addMessage(IChatParticipant p, String text) {
		ChatMessage message = new ChatMessage(p, text);
		addMessage(message);
	}

	public void addMessage(ChatMessage message) {
		this.messages.add(message);
	}

	public String getLog() {
		String chatLog = "";
		Iterator it = this.messages.iterator();
		while (it.hasNext()) {
			chatLog = it.next().toString() + "\n" + chatLog;

		}
		return chatLog;
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
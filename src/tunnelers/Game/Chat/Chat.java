package tunnelers.Game.Chat;

import generic.CyclicArray;
import java.util.Iterator;
import javafx.scene.paint.Color;
import tunnelers.Game.Frame.Player;

/**
 *
 * @author Stepan
 */
public class Chat {

	private final int MAX_MESSAGES = 12;

	private final CyclicArray<ChatMessage> messages;

	public Chat() {
		messages = new CyclicArray<>(ChatMessage.class, MAX_MESSAGES);
	}

	public void addMessage(Player p, String text) {
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
}
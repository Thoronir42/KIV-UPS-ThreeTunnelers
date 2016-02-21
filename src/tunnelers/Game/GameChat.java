package tunnelers.Game;

import generic.CyclicArray;
import java.util.Iterator;
import javafx.scene.paint.Color;
import tunnelers.Game.Frame.Player;

/**
 *
 * @author Stepan
 */
public class GameChat {

	private final int MAX_MESSAGES = 12;

	private final CyclicArray<ChatMessage> messages;

	public GameChat() {
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

class ChatMessage {

	private final Player player;
	private final String text;

	public ChatMessage(Player player, String message) {
		this.player = player;
		this.text = message;
	}

	@Override
	public String toString() {
		return String.format("%s : %s", player.getName(), text);
	}

	public Color getColor() {
		return this.player.getColor();
	}
}

package tunnelers.Game.Chat;


import javafx.scene.paint.Color;
import tunnelers.Game.Frame.Player;



public class ChatMessage {

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

package tunnelers.app.views.lobby;

import java.util.Iterator;
import tunnelers.app.render.colors.AColorScheme;
import tunnelers.app.render.colors.PlayerColors;
import tunnelers.core.chat.Chat;
import tunnelers.core.chat.ChatMessage;

/**
 *
 * @author Stepan
 */
public class ChatPrinter {

	private final Chat chat;
	private final AColorScheme colors;

	public ChatPrinter(Chat chat, AColorScheme colors){
		this.chat = chat;
		this.colors = colors;
		
	}
	
	public String getHtml() {
		StringBuilder sb = new StringBuilder();
		PlayerColors pc = colors.playerColors();

		Iterator<ChatMessage> it = chat.iterator();
		while (it.hasNext()) {
			ChatMessage msg = it.next();
			String rowHtml = String.format("<span><b style=\"color:#%s;\">%s</b>: %s</span><br/>", 
					pc.get(msg.getColor()), msg.getName(), msg.getText());
			sb.insert(0, rowHtml);
		}

		return sb.toString();
	}
	
}

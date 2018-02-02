package tunnelers.app.views.components.chat;

import tunnelers.app.render.colors.FxPlayerColorManager;
import tunnelers.core.chat.ChatMessage;

import java.util.Iterator;

public class ChatPrinter {
	private final FxPlayerColorManager colors;

	ChatPrinter(FxPlayerColorManager colors) {
		this.colors = colors;

	}

	public String getHtml(Iterator<ChatMessage> it) {
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			ChatMessage msg = it.next();
			String rowHtml = String.format("<span><b style=\"color:#%s;\">%s</b>: %s</span><br/>",
					colors.get(msg.getColor()).color(), msg.getName(), msg.getText());
			sb.insert(0, rowHtml);
		}

		return sb.toString();
	}

}

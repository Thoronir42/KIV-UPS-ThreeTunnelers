package tunnelers.app.views.components.chat;

import java.util.Iterator;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import tunnelers.app.render.colors.FxPlayerColorManager;
import tunnelers.core.chat.ChatMessage;

/**
 *
 * @author Stepan
 */
public class SimpleChat {

	protected WebView chatBox;
	protected TextField chatIn;
	protected ChatPrinter printer;

	protected EventHandler<ChatEvent> onMessageSend;

	public SimpleChat(FxPlayerColorManager colors) {
		this.chatBox = new WebView();
		this.chatIn = new TextField();
		this.printer = new ChatPrinter(colors);

		chatIn.setOnAction(event -> {
			this.sendMessage();
		});

	}

	public WebView box() {
		return this.chatBox;
	}

	public TextField input() {
		return chatIn;
	}

	public void setOnMessageSend(EventHandler<ChatEvent> onMessageSend) {
		this.onMessageSend = onMessageSend;
	}

	public void setContent(Iterator<ChatMessage> it) {
		this.chatBox.getEngine().loadContent(this.printer.getHtml(it));
	}

	public void sendMessage() {
		String message = this.chatIn.getText();
		if (message.length() > 0 && this.onMessageSend != null) {
			this.onMessageSend.handle(new ChatEvent(message));
		}
		this.chatIn.setText("");
	}
}

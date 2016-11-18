package tunnelers.app.views.components.chat;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

/**
 *
 * @author Stepan
 */
public class SimpleChat {

	protected WebView chatBox;
	protected TextField chatIn;

	protected EventHandler<ChatEvent> onMessageSend;

	public SimpleChat() {
		this.chatBox = new WebView();
		this.chatIn = new TextField();

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

	public void setContent(String html) {
		this.chatBox.getEngine().loadContent(html);
	}

	public void sendMessage() {
		String message = this.chatIn.getText();
		if (message.length() > 0 && this.onMessageSend != null) {
			this.onMessageSend.handle(new ChatEvent(message));
		}
		this.chatIn.setText("");
	}
}

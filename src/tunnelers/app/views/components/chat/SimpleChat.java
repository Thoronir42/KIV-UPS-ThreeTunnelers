package tunnelers.app.views.components.chat;

import java.util.Iterator;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import tunnelers.app.render.colors.FxPlayerColorManager;
import tunnelers.core.chat.ChatMessage;

/**
 *
 * @author Stepan
 */
public class SimpleChat extends GridPane {

	protected WebView chatBox;
	protected TextField chatIn;
	protected ChatPrinter printer;

	protected EventHandler<ChatEvent> onMessageSend;

	public SimpleChat(FxPlayerColorManager colors, boolean includeSendButton) {
		this.chatBox = new WebView();
		this.chatIn = new TextField();
		this.printer = new ChatPrinter(colors);

		this.chatBox.prefWidthProperty().bind(this.widthProperty());

		Parent actionArea;
		if (!includeSendButton) {
			this.chatIn.prefWidthProperty().bind(this.widthProperty());
			actionArea = this.chatIn;

		} else {
			HBox box = new HBox();
			Button btnSend = new Button("Odeslat");
			btnSend.setOnAction((e) -> {
				this.sendMessage();
			});
			this.chatIn.prefWidthProperty().bind(this.widthProperty().subtract(btnSend.widthProperty()));
			box.getChildren().addAll(this.chatIn, btnSend);
			actionArea = box;
		}

		this.add(this.chatBox, 0, 0);
		this.add(actionArea, 0, 1);

		this.chatIn.setOnAction(event -> {
			this.sendMessage();
		});

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

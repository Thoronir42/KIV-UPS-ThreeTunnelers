package tunnelers.app.views.debug;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import tunnelers.app.ATunnelersScene;

public class FlashTester extends GridPane {
	// fixme: tweak flash handling?
	public FlashTester(ATunnelersScene scene) {
		TextField txt_flash = new TextField();
		txt_flash.setPromptText("Zpráva k zobrazení");
		Button but_flashDisplay = new Button("Zobrazit");
		Button but_flashClear = new Button("Schovat");

		but_flashDisplay.setOnAction(e -> {
			if (!"".equals(txt_flash.getText().trim())) {
				scene.flashDisplay(txt_flash.getText());
				txt_flash.setText("");
			}
		});
		but_flashClear.setOnAction(e -> {
			scene.flashClear();
		});


		add(txt_flash, 0, 0, 2, 1);
		add(but_flashDisplay, 0, 1);
		add(but_flashClear, 1, 1);
	}

}

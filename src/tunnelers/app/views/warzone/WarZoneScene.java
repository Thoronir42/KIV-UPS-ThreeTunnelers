package tunnelers.app.views.warzone;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.controls.FxControlsManager;
import tunnelers.app.render.ARenderLayout;
import tunnelers.app.render.FxRenderContainer;
import tunnelers.core.settings.Settings;

public class WarZoneScene extends ATunnelersScene {

	protected TextArea ta_chatBox;
	protected TextField tf_chatIn;
	protected ARenderLayout layout;

	private final FxControlsManager csmgr;

	public WarZoneScene(Settings settings, FxControlsManager csmgr) {
		super(new BorderPane(), settings.getWindowWidth(), settings.getWindowHeight(), "Bitevní zóna");
		this.csmgr = csmgr;
		BorderPane root = (BorderPane) this.content;
		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.DIMGRAY.hashCode()));

		root.setCenter(this.canvas);

		this.setOnKeyPressed((KeyEvent e) -> csmgr.keyPressSet(e.getCode(), true));
		this.setOnKeyReleased((KeyEvent e) -> csmgr.keyPressSet(e.getCode(), false));
	}

	public WarZoneScene initLayout(int playerCount, FxRenderContainer renderer) {
		Dimension2D availableArea = new Dimension2D(canvas.getWidth(), canvas.getHeight());
		layout = ARenderLayout.choseIdeal(renderer, playerCount, availableArea);

		return this;
	}

	@Override
	public void update(long tick) {
		Platform.runLater(() -> layout.draw(canvas.getGraphicsContext2D()));
	}


}

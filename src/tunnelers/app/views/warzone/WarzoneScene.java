package tunnelers.app.views.warzone;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import tunnelers.app.controls.FxControlsManager;
import tunnelers.app.render.ARenderLayout;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.render.FxRenderContainer;

/**
 *
 * @author Stepan
 */
public class WarzoneScene extends ATunnelersScene {

	public static WarzoneScene getInstance(FxControlsManager csmgr) {
		return createInstance(csmgr);
	}

	private static WarzoneScene createInstance(FxControlsManager csmgr) {
		BorderPane root = new BorderPane();

		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.DIMGRAY.hashCode()));
		
		WarzoneScene scene = new WarzoneScene(root, settings.getWindowWidth(), settings.getWindowHeight(), csmgr);

		addComponents(root, scene);

		scene.setOnKeyPressed((KeyEvent e) -> {
			csmgr.keyPressSet(e.getCode(), true);
		});
		scene.setOnKeyReleased((KeyEvent e) -> {
			csmgr.keyPressSet(e.getCode(), false);
		});

		return scene;
	}

	private static void addComponents(BorderPane root, WarzoneScene scene) {
		root.setCenter(scene.canvas);
	}

	protected TextArea ta_chatBox;
	protected TextField tf_chatIn;
	protected ARenderLayout layout;
	
	private final FxControlsManager csmgr;

	public WarzoneScene(Region root, double width, double height, FxControlsManager csmgr) {
		super(root, width, height, "Bitevní zóna");
		this.csmgr = csmgr;
	}

	public WarzoneScene initLayout(int playerCount, FxRenderContainer renderer) {
		Dimension2D availableArea = new Dimension2D(canvas.getWidth(), canvas.getHeight());
		layout = ARenderLayout.choseIdeal(renderer, playerCount, availableArea);
		
		return this;
	}

	@Override
	public void update(long tick) {
		Platform.runLater(() -> {
			layout.draw(canvas.getGraphicsContext2D());
		});
	}
	
	
}

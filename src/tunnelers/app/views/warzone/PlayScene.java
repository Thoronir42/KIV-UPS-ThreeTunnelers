package tunnelers.app.views.warzone;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import tunnelers.app.controls.ControlsManager;
import tunnelers.app.render.RenderLayout;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.render.FxRenderHelper;

/**
 *
 * @author Stepan
 */
public class PlayScene extends ATunnelersScene {

	public static PlayScene getInstance(ControlsManager csmgr) {
		return createInstance(csmgr);
	}

	private static PlayScene createInstance(ControlsManager csmgr) {
		BorderPane root = new BorderPane();

		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.DIMGRAY.hashCode()));
		
		PlayScene scene = new PlayScene(root, settings.getWindowWidth(), settings.getWindowHeight(), csmgr);

		addComponents(root, scene);

		scene.setOnKeyPressed((KeyEvent e) -> {
			csmgr.keyPressSet(e.getCode(), true);
		});
		scene.setOnKeyReleased((KeyEvent e) -> {
			csmgr.keyPressSet(e.getCode(), false);
		});

		return scene;

	}

	private static void addComponents(BorderPane root, PlayScene scene) {
		root.setCenter(scene.canvas);
	}

	protected TextArea ta_chatBox;
	protected TextField tf_chatIn;
	protected RenderLayout layout;
	
	private final ControlsManager csmgr;

	public PlayScene(Parent root, double width, double height, ControlsManager csmgr) {
		super(root, width, height, "Bitevní zóna");
		this.csmgr = csmgr;
	}

	public void initLayout(int playerCount, FxRenderHelper renderer) {
		Dimension2D availableArea = new Dimension2D(canvas.getWidth(), canvas.getHeight());
		layout = RenderLayout.choseIdeal(playerCount, availableArea);
		layout.setRenderer(renderer);
	}


	@Override
	public void drawScene() {
		Platform.runLater(() -> {
			layout.draw(canvas.getGraphicsContext2D());
		});

	}

	@Override
	public Class getPrevScene() {
		return null;
	}
}

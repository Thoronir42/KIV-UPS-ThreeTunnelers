package tunnelers.app.views.warzone;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.core.io.AControls;
import tunnelers.Game.IO.ControlInput;
import tunnelers.core.io.InputAction;
import tunnelers.app.render.CanvasLayout;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.render.FxRenderer;
import tunnelers.core.engine.Engine;

/**
 *
 * @author Stepan
 */
public class PlayScene extends ATunnelersScene {

	private final Engine engine;
	
	public static PlayScene getInstance(Engine e, ControlSchemeManager csmgr, FxRenderer renderer) {
		return createInstance(e, csmgr, renderer);
	}

	private static PlayScene createInstance(Engine eng, ControlSchemeManager csmgr, FxRenderer renderer) {
		BorderPane root = new BorderPane();

		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.DIMGRAY.hashCode()));
		
		PlayScene scene = new PlayScene(root, settings.getWindowWidth(), settings.getWindowHeight(), eng, csmgr, renderer);

		addComponents(root, scene);

		scene.setOnKeyPressed((KeyEvent e) -> {
			scene.handleKey(e.getCode(), true);
		});
		scene.setOnKeyReleased((KeyEvent e) -> {
			scene.handleKey(e.getCode(), false);
		});

		return scene;

	}

	private static void addComponents(BorderPane root, PlayScene scene) {
		root.setCenter(scene.canvas);
	}

	protected TextArea ta_chatBox;
	protected TextField tf_chatIn;
	protected CanvasLayout canvasLayout;
	
	protected FxRenderer renderer;
	
	private final ControlSchemeManager csmgr;

	public PlayScene(Parent root, double width, double height, Engine e, ControlSchemeManager csmgr, FxRenderer renderer) {
		super(root, width, height, "Bitevní zóna");
		this.renderer = renderer;
		this.engine = e;
		this.csmgr = csmgr;
	}

	public void initLayout(int playerCount) {
		Dimension2D availableArea = new Dimension2D(canvas.getWidth(), canvas.getHeight());
		CanvasLayout layout = CanvasLayout.choseIdeal(playerCount, availableArea);
		
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		layout.setRenderer(this.renderer);
		
		this.canvasLayout = layout;
	}
	
	void handleKey(KeyCode code, boolean pressed) {
		ControlInput pi = this.csmgr.getPlayerInputByKeyPress(code);
		if(pi == null){
			return;
		}
		AControls controlSchemeId = pi.getControlScheme();
		InputAction inp = pi.getInput();
		
		this.engine.handleInput(inp, controlSchemeId.getPlayerID(), pressed);
	}


	@Override
	public void drawScene() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		Platform.runLater(() -> {
			canvasLayout.draw(g, this.engine.getPlayers());
		});

	}

	@Override
	public Class getPrevScene() {
		return null;
	}
}

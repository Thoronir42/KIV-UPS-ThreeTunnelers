package tunnelers.Game;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.Game.IO.ControlInput;
import tunnelers.Game.IO.InputAction;
import tunnelers.app.render.CanvasLayout;
import tunnelers.app.render.ZoneRenderer;
import tunnelers.core.GameContainer;
import tunnelers.Settings.Settings;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.render.AssetsRenderer;
import tunnelers.core.engine.Engine;

/**
 *
 * @author Stepan
 */
public class PlayScene extends ATunnelersScene {

	public static PlayScene getInstance(Engine e, ControlSchemeManager csmgr) {
		return createInstance(e, csmgr);
	}

	private static PlayScene createInstance(Engine eng, ControlSchemeManager csmgr) {
		BorderPane root = new BorderPane();

		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.DIMGRAY.hashCode()));
		
		PlayScene scene = new PlayScene(root, settings.getWindowWidth(), settings.getWindowHeight(), eng, csmgr);

		addComponents(root, scene, settings);

		scene.setOnKeyPressed((KeyEvent e) -> {
			scene.handleKey(e.getCode(), true);
		});
		scene.setOnKeyReleased((KeyEvent e) -> {
			scene.handleKey(e.getCode(), false);
		});

		return scene;

	}

	private static void addComponents(BorderPane root, PlayScene scene, Settings settings) {
		int chatWidth = 160;

		scene.ca_drawArea = new Canvas(settings.getWindowWidth() - chatWidth, settings.getWindowHeight());
		root.setCenter(scene.ca_drawArea);

		VBox vertical = new VBox();

		scene.ta_chatBox = new TextArea();
		scene.ta_chatBox.setWrapText(true);
		scene.ta_chatBox.setPrefWidth(chatWidth);
		scene.ta_chatBox.setPrefRowCount(10);
		scene.ta_chatBox.setEditable(false);
		scene.ta_chatBox.setBackground(Background.EMPTY);
		vertical.getChildren().add(scene.ta_chatBox);

		scene.tf_chatIn = new TextField();
		scene.tf_chatIn.setPrefWidth(chatWidth);
		scene.tf_chatIn.setDisable(true);
		vertical.getChildren().add(scene.tf_chatIn);
	}

	protected TextArea ta_chatBox;
	protected TextField tf_chatIn;
	protected Canvas ca_drawArea;
	protected CanvasLayout canvasLayout;
	private final Engine engine;
	
	private final ControlSchemeManager csmgr;

	public PlayScene(Parent root, double width, double height, Engine e, ControlSchemeManager csmgr) {
		super(root, width, height, "Battlefield");
		this.engine = e;
		this.csmgr = csmgr;
	}

	public void setCanvasLayout(GameContainer container) {
		Dimension2D availableArea = new Dimension2D(this.ca_drawArea.getWidth(), this.ca_drawArea.getHeight());
		CanvasLayout layout = CanvasLayout.choseIdeal(container.getPlayerCount(), availableArea);
		
		GraphicsContext g = this.ca_drawArea.getGraphicsContext2D();
		Dimension2D bs = layout.getBlockSize();
		
		layout.setZoneRenderer(new ZoneRenderer(g, bs, container.getWarzone().getMap()));
		layout.setAssetsRenderer(new AssetsRenderer(g, bs, ASSETS, container.getPlayers()));
		
		this.canvasLayout = layout;
	}
	
	void handleKey(KeyCode code, boolean pressed) {
		ControlInput pi = this.csmgr.getPlayerInputByKeyPress(code);
		if(pi == null){
			return;
		}
		AControlScheme controlSchemeId = pi.getControlScheme();
		InputAction inp = pi.getInput();
		
		this.engine.handleInput(inp, controlSchemeId.getPlayerID(), pressed);
	}


	@Override
	public void drawScene() {
		GraphicsContext g = this.ca_drawArea.getGraphicsContext2D();
		Platform.runLater(() -> {
			canvasLayout.draw(g, this.engine.getPlayers());
		});

	}

	@Override
	public Class getPrevScene() {
		return null;
	}
}

package tunnelers.Game;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tunnelers.Assets;
import tunnelers.Game.Render.CanvasLayout;
import tunnelers.Game.Render.Renderer;
import tunnelers.Game.Frame.Container;
import tunnelers.Settings;

/**
 *
 * @author Stepan
 */
public class PlayScene extends AGameScene {

	public static PlayScene getInstance(Container c) {
		return createInstance(c);
	}

	private static PlayScene createInstance(Container c) {
		BorderPane root = new BorderPane();

		root.setStyle("-fx-background-color: #" + Integer.toHexString(Color.DIMGRAY.hashCode()));
		
		Settings settings = Settings.getInstance();
		PlayScene scene = new PlayScene(root, settings.getWidth(), settings.getHeight());

		addComponents(root, scene, settings);

		scene.setOnKeyPressed((KeyEvent e) -> {
			scene.getStage().handleKey(e.getCode(), true);
		});
		scene.setOnKeyReleased((KeyEvent e) -> {
			scene.getStage().handleKey(e.getCode(), false);
		});
		scene.setCanvasLayout(c);

		return scene;

	}

	protected TextArea ta_chatBox;
	protected TextField tf_chatIn;
	protected Canvas ca_drawArea;
	protected CanvasLayout canvasLayout;

	public PlayScene(Parent root, double width, double height) {
		super(root, width, height, "Battlefield");

	}

	private static void addComponents(BorderPane root, PlayScene scene, Settings settings) {
		int chatWidth = 160;

		scene.ca_drawArea = new Canvas(settings.getWidth() - chatWidth, settings.getHeight());
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

		//root.setRight(vertical);
		root.setOnMouseClicked((MouseEvent e) -> {
			scene.drawScene();
		});
	}

	private void setCanvasLayout(Container container) {
		Dimension2D availableArea = new Dimension2D(this.ca_drawArea.getWidth(), this.ca_drawArea.getHeight());
		CanvasLayout layout = CanvasLayout.choseIdeal(container, availableArea);
		Assets assets = new Assets(container.getPlayers());
		Renderer renderer = new Renderer(this.ca_drawArea.getGraphicsContext2D(), container.getMap(), assets, layout.getBlockSize());
		layout.setRenderer(renderer);
		this.canvasLayout = layout;
	}

	@Override
	public void updateChatbox() {
		GameStage stage = this.getStage();
		this.ta_chatBox.setText(stage.getGamechat().getLog());
	}

	@Override
	public void drawScene() {
		GraphicsContext g = this.ca_drawArea.getGraphicsContext2D();
		Platform.runLater(() -> {
			canvasLayout.drawLayout(g);
		});

	}
}

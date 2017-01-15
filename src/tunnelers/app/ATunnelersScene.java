package tunnelers.app;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import tunnelers.core.settings.Settings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.AfterFX;
import tunnelers.app.views.components.flash.FlashAreaControl;
import tunnelers.app.views.components.flash.FlashContainer;
import tunnelers.common.IUpdatable;
import tunnelers.core.engine.Engine;
import tunnelers.core.engine.EngineUserInterface;

/**
 *
 * @author Stepan
 */
public abstract class ATunnelersScene extends Scene implements IUpdatable, IFlasher {

	protected static Assets ASSETS;
	private static int sceneCount = 0;

	protected static Settings settings = Settings.getInstance();

	protected String sceneName;

	protected Canvas canvas;

	protected FlashAreaControl flash;

	private AfterFX afterFx;

	private Rectangle2D canvasTarget;
	private Dimension2D blockSize;

	public ATunnelersScene(Parent root, double width, double height) {
		this(root, width, height, "scene " + (++sceneCount));
	}

	public ATunnelersScene(Parent content, double width, double height, String name) {
		super(new StackPane(), width, height);
		this.sceneName = name;

		this.setOnKeyPressed((KeyEvent event) -> {
			handleKeyPressed(event.getCode());
		});

		canvas = new Canvas();
		canvas.widthProperty().bind(this.widthProperty());
		canvas.heightProperty().bind(this.heightProperty());
		
		FlashContainer flashContainer = FlashContainer.getInstance();
		flash = new FlashAreaControl(flashContainer);
		System.out.format("New flash area control created for %s with %s\n", this.getClass().getSimpleName(), flashContainer.getMessage());

		StackPane root = ((StackPane) this.getRoot());

		AnchorPane anchor = new AnchorPane(flash);
		anchor.prefWidthProperty().bind(root.widthProperty());
		flash.prefWidthProperty().bind(anchor.widthProperty());
		flash.visibilityProperty().addListener((observable, oldValue, newValue) -> {
			AnchorPane.setTopAnchor(flash, (newValue.floatValue() - 1) * flash.getHeight());
		});

		flash.clear();

		root.getChildren().addAll(canvas, anchor, content);

		canvasTarget = new Rectangle2D(0, 0, canvas.getWidth(), canvas.getHeight());
		blockSize = new Dimension2D(40, 40);
	}

	public void setName(String name) {
		this.sceneName = name;
	}

	public String getName() {
		return sceneName;
	}

	public void handleKeyPressed(KeyCode code) {
		switch (code) {
			case ESCAPE:
				this.getStage().prevScene();
				break;
		}
	}

	@Override
	public void update(long tick) {
		if (this.flash.updateVisibility()) {
			//this.reanchorFlash();
		}

		if (this.afterFx != null) {
			Platform.runLater(() -> {
				GraphicsContext g = this.getGraphicsContext();
				this.afterFx.renderStaticNoise(g, canvasTarget, 0.12, blockSize);
			});

		}

	}

	protected void setAfterFX(AfterFX afterFx) {
		this.afterFx = afterFx;
	}

	protected TunnelersStage getStage() {
		return (TunnelersStage) this.getWindow();
	}

	protected EngineUserInterface getEngine() {
		return this.getStage().engine;
	}

	public GraphicsContext getGraphicsContext() {
		return this.canvas.getGraphicsContext2D();
	}

	@Override
	public void flashDisplay(String message) {
		System.out.println(this.getClass().getSimpleName() + " displays flash " + message);
		this.flash.display(message);
	}

	@Override
	public void flashClear() {
		this.flash.clear();
	}
}

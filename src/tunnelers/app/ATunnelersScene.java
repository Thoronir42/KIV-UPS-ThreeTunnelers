package tunnelers.app;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Rectangle2D;
import tunnelers.core.settings.Settings;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.AfterFX;
import tunnelers.app.views.components.flash.FlashAreaControl;
import tunnelers.common.IUpdatable;
import tunnelers.core.engine.EngineUserInterface;

/**
 *
 * @author Stepan
 */
public abstract class ATunnelersScene extends Scene implements IUpdatable, IFlasher {

	protected static Assets ASSETS;

	protected static Settings settings = Settings.getInstance();

	protected String sceneName;

	protected FlashAreaControl flash;

	protected Canvas canvas;

	private AfterFX afterFx;
	private final Rectangle2D canvasTarget;
	private final Dimension2D blockSize;

	public ATunnelersScene(Region content, double width, double height, String name) {
		super(new StackPane(), width, height);
		this.sceneName = name;

		this.setOnKeyPressed((KeyEvent event) -> {
			handleKeyPressed(event.getCode());
		});

		flash = FlashAreaControl.getInstance();

		StackPane root = ((StackPane) this.getRoot());

		canvas = new Canvas();
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());

		AnchorPane anchor = new AnchorPane(content, flash);
//		anchor.prefWidthProperty().bind(root.widthProperty());
		flash.prefWidthProperty().bind(root.widthProperty());

		content.prefWidthProperty().bind(root.widthProperty());
		content.prefHeightProperty().bind(root.heightProperty());

		flash.visibilityProperty().addListener((observable, oldValue, newValue) -> {
			this.setFlashVisibility(newValue.floatValue());
		});

		root.getChildren().addAll(canvas, anchor);

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
			default:
				System.out.println("Key pressed not handled");
		}
	}

	@Override
	public void update(long tick) {
		this.flash.updateVisibility();

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
		this.flashDisplay(message, 10);
	}

	public void flashDisplay(String message, int seconds) {
		System.out.println(this.getClass().getSimpleName() + " displays flash " + message);
		this.flash.display(message, seconds);
	}

	@Override
	public void flashClear() {
		this.flash.clear();
	}

	public void flashClear(boolean immediately) {
		this.flash.clear(immediately);
	}

	private void setFlashVisibility(float value) {
		AnchorPane.setTopAnchor(flash, (value - 1) * flash.getHeight());
	}
}

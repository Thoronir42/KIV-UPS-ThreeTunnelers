package tunnelers.app;

import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import tunnelers.app.assets.Assets;
import tunnelers.app.render.AfterFX;
import tunnelers.app.views.components.flash.FlashAreaControl;
import tunnelers.core.engine.EngineUserInterface;
import tunnelers.core.settings.Settings;

public abstract class ATunnelersScene extends Scene implements IFlasher {

	private static final Color NOISE_TINT = new Color(0.98, 0.98, 0.98, 0.1);

	protected final Region content;

	protected String sceneName;

	protected final FlashAreaControl flash;

	protected final Canvas canvas;

	private AfterFX afterFx;
	private final Dimension2D blockSize;

	public ATunnelersScene(Region content, double width, double height, String name) {
		super(new StackPane(), width, height);
		StackPane root = ((StackPane) this.getRoot());

		this.setName(name);
		this.content = content;


		this.setOnKeyPressed((KeyEvent event) -> handleKeyPressed(event.getCode()));

		flash = FlashAreaControl.getInstance();



		canvas = new Canvas();
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());

		AnchorPane anchor = new AnchorPane(content, flash);
//		anchor.prefWidthProperty().bind(root.widthProperty());
		flash.prefWidthProperty().bind(root.widthProperty());

		content.prefWidthProperty().bind(root.widthProperty());
		content.prefHeightProperty().bind(root.heightProperty());

		flash.visibilityProperty().addListener((observable, oldValue, newValue)
				-> this.setFlashVisibility(newValue.floatValue()));

		root.getChildren().addAll(canvas, anchor);

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

	public void update(long tick) {
		this.flash.updateVisibility();

		if (this.afterFx != null) {
			Platform.runLater(() -> {
				GraphicsContext g = this.getGraphicsContext();
				this.afterFx.renderStaticNoise(g, blockSize, 0.12, canvas.getWidth(), canvas.getHeight());
				g.setFill(NOISE_TINT);
				g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
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

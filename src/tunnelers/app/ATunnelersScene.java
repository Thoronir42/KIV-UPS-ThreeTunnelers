package tunnelers.app;

import generic.RNG;
import tunnelers.core.settings.Settings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import tunnelers.app.assets.Assets;
import tunnelers.app.views.components.flash.FlashArea;
import tunnelers.common.IUpdatable;
import tunnelers.core.engine.Engine;

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

	protected FlashArea flash;

	public void setName(String name) {
		this.sceneName = name;
	}

	public String getName() {
		return sceneName;
	}

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

		flash = new FlashArea();

		AnchorPane anchor = new AnchorPane(flash);

		StackPane root = ((StackPane) this.getRoot());
		root.getChildren().addAll(canvas, anchor, content);

		flash.widthProperty().addListener((l, o, n) -> {
			reanchorFlash();
		});
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
			this.reanchorFlash();
		}

		int x1 = RNG.getRandInt((int) this.getWidth()),
				y1 = RNG.getRandInt((int) this.getHeight());
		int x2 = RNG.getRandInt((int) this.getWidth() - x1),
				y2 = RNG.getRandInt((int) this.getHeight() - y1);

		GraphicsContext g = this.getGraphicsContext();
		if (tick % 20 == 0) {
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
		}

		g.setStroke(Color.BLACK);
		g.setLineWidth(2);
		g.strokeRect(x1, y1, x2, y2);

	}

	private void reanchorFlash() {
		float visibility = this.flash.getVisibility();
		AnchorPane.setTopAnchor(flash, (visibility - 1) * flash.getHeight());

		double diff = this.getWidth() - flash.getWidth();
//		System.out.format("%.1f - %.1f = %.1f\n", this.widthProperty().get(), flash.getWidth(), diff);
//		System.out.format("%.1f / 2.0 = %.1f\n", diff, diff / 2.0);

		AnchorPane.setLeftAnchor(flash, diff / 2);
	}

	protected TunnelersStage getStage() {
		return (TunnelersStage) this.getWindow();
	}

	protected Engine getEngine() {
		return this.getStage().engine;
	}

	public GraphicsContext getGraphicsContext() {
		return this.canvas.getGraphicsContext2D();
	}

	@Override
	public void flashDisplay(String message) {
		this.flash.display(message);
	}

	@Override
	public void flashClear() {
		this.flash.clear();
	}
}

package tunnelers.app;

import tunnelers.core.settings.Settings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tunnelers.app.assets.Assets;

/**
 *
 * @author Stepan
 */
public abstract class ATunnelersScene extends Scene {

	protected static Assets ASSETS;
	private static int sceneCount = 0;

	protected static Settings settings = Settings.getInstance();

	protected String sceneName;
	
	protected Canvas canvas;
	

	public void setName(String name) {
		this.sceneName = name;
	}

	public String getName() {
		return sceneName;
	}

	public ATunnelersScene(Parent root, double width, double height) {
		this(root, width, height, "scene " + (++sceneCount));
	}

	public ATunnelersScene(Parent root, double width, double height, String name) {
		super(root, width, height);
		this.sceneName = name;
		
		this.setOnKeyPressed((KeyEvent event) -> {
			handleKeyPressed(event.getCode());
		});
	}
	
	public void handleKeyPressed(KeyCode code) {
		switch (code) {
			case ESCAPE:
				this.goBack();
				break;
		}
	}
	
	public void drawScene(){
		
	}
	
	protected TunnelersStage getStage() {
		return (TunnelersStage) this.getWindow();
	}
	
	protected void goBack(){
		this.getStage().prevScene();
	}

	public abstract Class getPrevScene();

	public GraphicsContext getGraphicsContext(){
		return this.canvas.getGraphicsContext2D();
	}
}

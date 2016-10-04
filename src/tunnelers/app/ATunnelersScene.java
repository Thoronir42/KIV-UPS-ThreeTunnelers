package tunnelers.app;

import tunnelers.Settings.Settings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import tunnelers.network.NetWorks;

/**
 *
 * @author Stepan
 */
public abstract class ATunnelersScene extends Scene {

	private static int sceneCount = 0;

	protected Settings settings = Settings.getInstance();

	protected String sceneName;

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
	
	protected NetWorks getNetworks() {
		return this.getStage().networks;
	}
	
	protected TunnelersStage getStage() {
		TunnelersStage stage = (TunnelersStage) this.getWindow();
		return stage;
	}
	
	protected void goBack(){
		this.getStage().prevScene();
	}

	public abstract Class getPrevScene();
	
}

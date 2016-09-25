package tunnelers;

import tunnelers.Settings.Settings;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
	}
}

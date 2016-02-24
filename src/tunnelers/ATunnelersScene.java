package tunnelers;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author Stepan
 */
public abstract class ATunnelersScene extends Scene {

	private static int sceneCount = 0;

	protected Settings settings = Settings.getInstance();

	protected String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ATunnelersScene(Parent root, double width, double height) {
		this(root, width, height, "scene " + (++sceneCount));
	}

	public ATunnelersScene(Parent root, double width, double height, String name) {
		super(root, width, height);
		this.name = name;
	}
}

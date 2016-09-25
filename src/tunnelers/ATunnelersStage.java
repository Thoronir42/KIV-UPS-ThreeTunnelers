package tunnelers;

import tunnelers.Settings.Settings;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.Stage;

/**
 *
 * @author Stepan
 */
public abstract class ATunnelersStage extends Stage {

	protected static final Settings SETTINGS = Settings.getInstance();

	public static final int CLOSE = 1,
			CHANGE_TO_MENU = 2,
			CHANGE_TO_GAME = 4;

	protected int returnCode = 0;

	public abstract void update(long tick);

	public ATunnelersStage() {
		
	}

	public void exit() {
		this.exit(CLOSE);
	}

	public void exit(int exitValue) {
		this.returnCode = exitValue;
		this.close();
	}

	public int getReturnCode() {
		return returnCode;
	}

	protected void changeScene(ATunnelersScene scene) {
		this.setScene(scene);
		this.setTitle(String.format("%s %s %s", SETTINGS.getGameName(), SETTINGS.getTitleSeparator(), scene.getName()));
	}

	public final void changeScene(Class reqScene) {
		ATunnelersScene scene = classToInstance(reqScene);
		if (scene == null) {
			return;
		}
		changeScene(scene);
	}

	protected ATunnelersScene classToInstance(Class scene) {
		if (scene == null) {
			return null;
		}
		if (!ATunnelersScene.class.isAssignableFrom(scene)) {
			System.out.format("%s not assignable from %s\n", scene.getSimpleName(), ATunnelersScene.class.getSimpleName());
			return null;
		}
		try {
			return invokeInstance(scene);
		} catch (IllegalAccessException | NoSuchMethodException |
				IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			System.err.format("Couldn't get instance of new scene: %s=%s\n", e.getClass().getSimpleName(), e.getMessage());
		}
		return null;
	}

	protected ATunnelersScene invokeInstance(Class scene) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (ATunnelersScene) scene.getDeclaredMethod("getInstance").invoke(null);
	}

}

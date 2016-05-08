package tunnelers.Game;

import javafx.scene.Parent;
import tunnelers.ATunnelersScene;

/**
 *
 * @author Stepan
 */
public abstract class AGameScene extends ATunnelersScene {

	public AGameScene(Parent root, double width, double height, String name) {
		super(root, width, height);
		this.name = name;
	}

	protected GameStage getStage() {
		GameStage stage = (GameStage) this.getWindow();
		return stage;
	}

	abstract void updateChatbox();

	public abstract void drawScene();
	
	protected void sendChatMessage(String message){
		this.getStage().getNetworks().sendMessage(message);
		this.getStage().getGamechat().addMessage(message);
		this.updateChatbox();
	}
}

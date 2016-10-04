package tunnelers.Game;

import javafx.scene.Parent;
import tunnelers.app.ATunnelersScene;

/**
 *
 * @author Stepan
 */
public abstract class AGameScene extends ATunnelersScene {

	public AGameScene(Parent root, double width, double height, String name) {
		super(root, width, height);
		this.sceneName = name;
	}

	

	abstract void updateChatbox();
	
	protected void sendChatMessage(String message){
		this.getNetworks().sendMessage(message);
		this.getGamechat().addMessage(message);
		this.updateChatbox();
	}
}

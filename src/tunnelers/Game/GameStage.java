package tunnelers.Game;

import tunnelers.Game.Chat.Chat;
import tunnelers.app.ATunnelersScene;
import tunnelers.app.TunnelersStage;
import tunnelers.core.GameContainer;
import javafx.scene.input.KeyCode;
import tunnelers.Game.IO.InputAction;
import tunnelers.Game.IO.ControlInput;
import tunnelers.model.player.APlayer;
import tunnelers.Game.IO.AControlScheme;
import tunnelers.network.command.GameCommand;
import tunnelers.network.command.NCG;

/**
 *
 * @author Stepan
 * @deprecated 
 */
public class GameStage extends TunnelersStage {

	public GameStage() {
		GameContainer container = GameContainer.mockContainer(this.controlSchemeManager, "KAREL");
	}

	void handleKey(KeyCode code, boolean pressed) {
		ControlInput pi = this.controlSchemeManager.getPlayerInputByKeyPress(code);
		if(pi == null){
			return;
		}
		AControlScheme controlSchemeId = pi.getControlScheme();
		InputAction inp = pi.getInput();
		
		APlayer p = this.engine.getPlayer(controlSchemeId.getPlayerID());
		
		if (p.getControls().handleControl(inp, pressed)) {
			NCG.NetCommand cmd = new GameCommand.ControlSet(inp.intVal(), pressed ? 1 : 0);
		}
	}

	protected void beginGame() {
		ATunnelersScene sc = PlayScene.getInstance(engine.getContainer());
		this.changeScene(sc);
	}
}

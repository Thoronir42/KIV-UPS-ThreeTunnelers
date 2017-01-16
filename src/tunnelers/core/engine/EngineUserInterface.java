package tunnelers.core.engine;

import temp.Mock;
import tunnelers.core.colors.PlayerColorManager;
import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.player.controls.Controls;
import tunnelers.core.player.controls.InputAction;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Skoro
 */
public class EngineUserInterface {
	private final Engine engine;
	
	public EngineUserInterface(Engine engine){
		this.engine = engine;
	}
	
	public void connect(String name, String addr, int port) {
		engine.view.setConnectEnabled(false);
		engine.netadapter.connectTo(engine.connectionSecret, name, addr, port);
	}

	public void disconnect() {
		engine.netadapter.disconnect("Disconnecting");
		engine.view.showScene(IView.Scene.MainMenu);
	}
	
	public void refreshServerList() {
		int n = 16;
		String lobbiesString = Mock.serverListString(n);
		IGameRoomInfo[] rooms = engine.gameRoomParser.parse(n, lobbiesString.substring(4));
		engine.view.appendGameRoomsToList(rooms);
	}
	
	public void joinGame(IGameRoomInfo gameRoom) {
		if (gameRoom.isFull()) {
			engine.view.alert("Hra je již plná");
			return;
		}
		PlayerColorManager playerColorManager = engine.view.getPlayerColorManager();
		playerColorManager.resetColorUsage();

		// TODO: link this through network events
		engine.currentGameRoom = Mock.gameRoom(engine.controls, playerColorManager);

		engine.view.alert("Probíhá připojování");

		engine.view.showScene(IView.Scene.Lobby);
	}
	
	public void beginGame(){
		engine.beginGame();
	}
	
	public void handleInput(InputAction inp, int controlsID, boolean pressed) {
		Controls controlsScheme = engine.controls.getScheme((byte) controlsID);

		if (controlsScheme.setControlState(inp, pressed)) {
			Command cmd = engine.netadapter.createCommand(CommandType.GameControlsSet);

		}
	}

	public void sendPlainText(String text) {
		Command cmd = engine.netadapter.createCommand(CommandType.MsgPlain);
		cmd.setData(text);
		
		engine.netadapter.send(cmd);
	}
	
	public GameRoom getGameRoom(){
		return engine.getGameRoom();
	}

	public String getHostLocator() {
		return engine.netadapter.getHostLocator();
	}
}

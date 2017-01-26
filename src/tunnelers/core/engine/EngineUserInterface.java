package tunnelers.core.engine;

import tunnelers.core.gameRoom.GameRoom;
import tunnelers.core.gameRoom.IGameRoomInfo;
import tunnelers.core.player.Player;
import tunnelers.core.player.controls.InputAction;
import tunnelers.network.command.Command;
import tunnelers.network.command.CommandType;

/**
 *
 * @author Skoro
 */
public class EngineUserInterface {

	private final Engine engine;

	public EngineUserInterface(Engine engine) {
		this.engine = engine;
	}

	public void connect(String name, String addr, int port, boolean useSecret) {
		engine.view.setConnectEnabled(false);
		if(!useSecret){
			engine.connectionSecret.set("");
		}
		engine.netadapter.connectTo(engine.connectionSecret, addr, port);
		
		engine.preferredName = name;
	}

	public void disconnect() {
		engine.netadapter.disconnect("Disconnecting");
		engine.view.showScene(IView.Scene.MainMenu);
	}

	public void refreshServerList() {
		Command lobbyList = engine.netadapter.createCommand(CommandType.RoomsList);
		engine.netadapter.send(lobbyList);
	}

	public void createRoom() {
		Command createRoom = engine.netadapter.createCommand(CommandType.RoomsCreate);
		engine.netadapter.send(createRoom);
	}

	public void joinRoom(IGameRoomInfo gameRoom) {
		if (gameRoom.isFull()) {
			engine.view.alert("Hra je již plná");
			return;
		}

		Command cmd = engine.netadapter.createCommand(CommandType.RoomsJoin);
		cmd.append((byte) gameRoom.getId());
		engine.netadapter.send(cmd);

		engine.view.alert("Probíhá připojování");

		engine.view.showScene(IView.Scene.Lobby);
	}
	
	public void leaveRoom() {
		engine.setStage(Engine.Stage.Menu);
		engine.currentGameRoom = null;
		
		Command leaver = engine.netadapter.createCommand(CommandType.RoomsLeave);
		engine.netadapter.send(leaver);
	}
	
	public void setReady(boolean value){
		Command readyState = engine.netadapter
				.createCommand(CommandType.RoomReadyState)
				.append((byte)(value ? 1 : 0));
		
		engine.netadapter.send(readyState);
	}

	public void beginGame() {
		Command mapPrep = engine.netadapter
				.createCommand(CommandType.MapSpecification)
				.append((byte) 20) // chunkSize
				.append((byte) 12) // xChunks
				.append((byte) 8); // yChunks

		// fixme: link through network
		engine.handle(mapPrep);

		engine.view.setGameData(engine.currentGameRoom.getMap(), engine.currentGameRoom.getPlayers());

		engine.setStage(Engine.Stage.Warzone);
		engine.view.showScene(IView.Scene.Warzone);
	}

	public void handleInput(InputAction inp, int controlsID, boolean pressed) {
		Player p = engine.localClient.getPlayer(controlsID);
		int playerRID = engine.currentGameRoom.getPlayerRID(p);
		
		if (p.getControls().set(inp, pressed)) {
			Command cmd = engine.netadapter.createCommand(CommandType.GameControlsSet)
					.append((byte)playerRID)
					.append((byte)p.getControls().getState());
			this.engine.netadapter.send(cmd);
		}
	}

	public void sendPlainText(String text) {
		Command cmd = engine.netadapter.createCommand(CommandType.MsgPlain);
		cmd.setData(text);

		engine.netadapter.send(cmd);
	}
	
	//    ACCESSORS

	public GameRoom getGameRoom() {
		return engine.getGameRoom();
	}

	public String getHostLocator() {
		return engine.netadapter.getHostLocator();
	}
}

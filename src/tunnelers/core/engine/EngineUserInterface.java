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

	public EngineUserInterface(Engine engine) {
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
		Command lobbyList = engine.netadapter.createCommand(CommandType.RoomsList);
		lobbyList.setData(Mock.serverListString(16));
		engine.handle(lobbyList);

		// todo: sent request instead of building lobby list
	}

	public void joinGame(IGameRoomInfo gameRoom) {
		if (gameRoom.isFull()) {
			engine.view.alert("Hra je již plná");
			return;
		}
		PlayerColorManager playerColorManager = engine.view.getPlayerColorManager();
		playerColorManager.resetColorUsage();

		// TODO: link this through network events
		// TODO: move to integration test 
		Command cmd = engine.netadapter.createCommand(CommandType.RoomsJoin)
				.append((byte) gameRoom.getId())
				.append((byte) 1) // localClientRID
				.append((byte) 1);// leaderClientRID
		engine.handle(cmd);
		cmd = engine.netadapter.createCommand(CommandType.RoomPlayerAttach)
				.append((byte) 1) // playerRID
				.append((byte) playerColorManager.useRandomColor().intValue())
				.append((byte) 1);// clientRID
		engine.handle(cmd);

		// client 2
		cmd = engine.netadapter.createCommand(CommandType.RoomClientInfo)
				.append((byte) 2)
				.append("John");
		engine.handle(cmd);
		cmd = engine.netadapter.createCommand(CommandType.RoomPlayerAttach)
				.append((byte) 2)
				.append((byte) playerColorManager.useRandomColor().intValue())
				.append((byte) 2);
		engine.handle(cmd);

		// client 3
		cmd = engine.netadapter.createCommand(CommandType.RoomClientInfo)
				.append((byte) 3)
				.append("Rakett");
		engine.handle(cmd);
		cmd = engine.netadapter.createCommand(CommandType.RoomPlayerAttach)
				.append((byte) 3)
				.append((byte) playerColorManager.useRandomColor().intValue())
				.append((byte) 3);
		engine.handle(cmd);

		engine.view.alert("Probíhá připojování");

		engine.view.showScene(IView.Scene.Lobby);
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
		engine.view.showScene(IView.Scene.Game);
	}

	public void handleInput(InputAction inp, int controlsID, boolean pressed) {
		Controls controlsScheme = engine.controls.getScheme((byte) controlsID);

		if (controlsScheme.set(inp, pressed)) {
			Command cmd = engine.netadapter.createCommand(CommandType.GameControlsSet);

		}
	}

	public void sendPlainText(String text) {
		Command cmd = engine.netadapter.createCommand(CommandType.MsgPlain);
		cmd.setData(text);

		engine.netadapter.send(cmd);
	}

	public GameRoom getGameRoom() {
		return engine.getGameRoom();
	}

	public String getHostLocator() {
		return engine.netadapter.getHostLocator();
	}
}

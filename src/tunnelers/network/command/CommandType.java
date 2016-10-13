package tunnelers.network.command;

/**
 *
 * @author Stepan
 */
public enum CommandType {
	Undefined(0),
	
	LeadApprove(1),
	LeadDeny(2),
	LeadStillThere(3),
	LeadWrongRoom(4),
	
	ConFetchLobbies(10),
	ConCreateGame(11),
	ConJoinGame(12),
	ConDisconnect(49),
	
	ConIncorrectPhase(80),
	ConGamesList(90),
	ConGamesListEnd(91),
	ConPlayerDisconnected(99),
	
	MsgPlain(100),
	MsgRcon(105),
	
	LobbyIntroduce(112),
	LobbySetColor(113),
	LobbySetLeader(120),
	LobbyGameStarts(130),
	GameVerifyChunk(131),
	LobbyGameStarted(132),
	GameKickPlayer(149),
	GamePlayerJoined(150),
	GamePlayerLeft(199),
	
	GameControlsSet(201),
	GameRequestChunk(210),
	GameRequestPlayersInfo(220),
	GameChunkData(310),
	GamePlayerInfo(320),
	
	;
	
	private final int intVal;
	
	private CommandType(int v){
		this.intVal = v;
	}
}

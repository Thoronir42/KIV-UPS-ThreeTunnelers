package tunnelers.network.command;

public enum CommandType {
	Undefined(0),
	//    LEAD
	LeadApprove(1),
	LeadDeny(2),
	LeadStillThere(3),
	LeadWrongRoom(4),
	//    CONNECTION
	ConFetchLobbies(10),
	ConCreateGame(11),
	ConJoinGame(12),
	ConDisconnect(49),
	ConIncorrectPhase(80),
	ConGamesList(90),
	ConGamesListEnd(91),
	ConPlayerDisconnected(99),
	//    MESSAGE
	MsgPlain(100),
	MsgRcon(105),
	//    LOBBY CONTROLS
	LobbyIntroduce(112),
	LobbySetColor(113),
	LobbySetLeader(120),
	LobbyGameStarts(130),
	GameVerifyChunk(131),
	LobbyGameStarted(132),
	GameKickPlayer(149),
	GamePlayerJoined(150),
	GamePlayerLeft(199),
	//    GAME CONTROLS
	GameControlsSet(201),
	GameRequestChunk(210),
	GameRequestPlayersInfo(220),
	GameChunkData(310),
	GamePlayerInfo(320),
	;

	private final short value;

	private CommandType(int v) {
		this((short) v);
	}

	private CommandType(short v) {
		this.value = v;
	}
	
	public short value(){
		return value;
	}
}

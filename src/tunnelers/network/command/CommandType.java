package tunnelers.network.command;

public enum CommandType {
	VirtConnectionTerminated(-4),
	VirtConnectingError(-3),
	VirtConnectingTimedOut(-2),
	VirtConnectionEstabilished(-1),
	
	Undefined(0),
	//    LEAD
	LeadApprove(1),
	LeadDeny(2),
	LeadStillThere(3),
	LeadBadFormat(4),
	LeadIntroduce(5),
	LeadReintroduce(6),
	//    CONNECTION
	ConFetchGameList(10),
	ConCreateGame(11),
	ConJoinGame(12),
	ConReconnect(13),
	ConDisconnect(20),
	ConGamesList(25),
	ConGamesListEnd(26),
	//    MESSAGE
	MsgPlain(40),
	MsgRcon(45),
	//    ROOM CONTROLS
	RoomSetCurrentPhase(110),
	RoomGetClientInfo(111),
	RoomClientIntroduce(112),
	RoomSetColor(113),
	RoomKickClient(118),
	RoomClientLeft(119),
	RoomSetLeader(120),
	RoomStartGame(125),
	RoomGameStarted(126),
	// GAME-MAP CONTROLS
	GameChunkRequest(140),
	GameChunkData(141),
	GameVerifyChunk(142),
	//    GAME-ENTITY CONTROLS
	GameControlsSet(201),
	GameTankRequest(210),
	GameTankInfo(211),
	GameProjAdd(220),
	GameProjRem(221),;

	private final short value;

	private CommandType(int v) {
		this((short) v);
	}

	private CommandType(short v) {
		this.value = v;
	}

	public short value() {
		return value;
	}
}

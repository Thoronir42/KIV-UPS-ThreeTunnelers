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
	RoomSyncPhase(100),
	RoomControlSignal(101),
	//    CLIENT RELATED COMMANDS
	RoomGetClientInfo(110),
	RoomClientIntroduce(111),
	RoomClientLeft(112),
	//    CLIENT CONTROLLING COMMANDS
	RoomKickClient(120),
	RoomSetLeader(121),
	//    PLAYER CONTROLLING COMMANDS
	PlayerAttach(130),
	PlayerDetach(131),
	PlayerMove(132),
	PlayerSetColor(133),
	//    MAP COMMANDS
	MapSpecification(140),
	MapChunkData(141),
	MapChunkRequest(142),
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

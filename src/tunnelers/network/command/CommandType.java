package tunnelers.network.command;

public enum CommandType {
	Undefined(0),
	//    LEAD
	LeadIntroduce(1),
	LeadDisconnect(2),
	LeadMarco(3),
	LeadPolo(4),
	LeadBadFormat(5),
	//    CLIENT
	ClientSetName(10),
	
	RoomsList(14),
	RoomsCreate(16),
	RoomsJoin(17),
	RoomsLeave(18),
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
	MapBlocksChanges(143),
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

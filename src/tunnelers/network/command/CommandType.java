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
	MsgRcon(42),
	//    ROOM CONTROLS
	RoomSyncPhase(60),
	RoomReadyState(61),
	//    CLIENT RELATED COMMANDS
	RoomClientInfo(65),
	RoomClientLatency(66),
	RoomClientRemove(67),
	RoomSetLeader(68),
	//    PLAYER CONTROLLING COMMANDS
	PlayerAttach(80),
	PlayerDetach(81),
	PlayerMove(82),
	PlayerSetColor(83),
	//    MAP COMMANDS
	MapSpecification(90),
	MapChunkData(91),
	MapBlocksChanges(92),
	//    GAME-ENTITY CONTROLS
	GameControlsSet(120),
	GameTankInfo(130),
	GameProjAdd(140),
	GameProjRem(141),;

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

package tunnelers.network.command;

public enum CommandType {
	Undefined(0x00),
	//    LEAD
	LeadIntroduce(0x01),
	LeadDisconnect(0x02),
	ClientSetName(0x03),
	LeadMarco(0x04),
	LeadPolo(0x05),
	
	RoomsList(0x10),
	RoomsCreate(0x12),
	RoomsJoin(0x13),
	RoomsLeave(0x14),
	//    MESSAGE
	MsgPlain(0x20),
	MsgSystem(0x21),
	MsgRcon(0x22),
	//    ROOM CONTROLS
	RoomSyncState(0x40),
	RoomReadyState(0x41),
	//    CLIENT RELATED COMMANDS
	RoomClientInfo(0x42),
	RoomClientStatus(0x43),
	RoomClientRemove(0x44),
	RoomSetLeader(0x45),
	//    PLAYER CONTROLLING COMMANDS
	RoomPlayerAttach(0x50),
	RoomPlayerDetach(0x51),
	RoomPlayerMove(0x52),
	RoomPlayerSetColor(0x53),
	//    MAP COMMANDS
	MapSpecification(0x60),
	MapBases(0x61),
	MapChunkData(0x62),
	MapBlocksChanges(0x63),
	//    GAME-ENTITY CONTROLS
	GameControlsSet(0x70),
	GameTankInfo(0x74),
	GameProjAdd(0x7A),
	GameProjRem(0x7B),;

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

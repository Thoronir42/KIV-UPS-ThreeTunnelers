package tunnelers.core.model.map;

import java.util.HashMap;

/**
 *
 * @author Stepan
 */
public enum Block {
	Empty(0),
	Dirt(1),
	Rock(2),
	BaseWall(3),
	Undefined(15);
	
	private static final HashMap<Byte, Block> typeMap;
	
	public static Block fromByteValue(byte c) {
		return typeMap.getOrDefault(c, Undefined);
	}

	static{
		typeMap = new HashMap<>();
		for(Block type : Block.values()){
			typeMap.put(type.byteValue(), type);
		}
	}
	
	private final byte typeValue;

	private Block(byte type) {
		this.typeValue = type;
	}

	private Block(int type) {
		this((byte) type);
	}

	public boolean isBreakable() {
		return this == Dirt;
	}
	
	public boolean isObstacle(){
		return this == BaseWall || this == Rock || this == Undefined;
	}
	
	public byte byteValue(){
		return this.typeValue;
	}
}

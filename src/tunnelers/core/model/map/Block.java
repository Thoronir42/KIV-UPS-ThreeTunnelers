package tunnelers.core.model.map;

import java.util.HashMap;

public enum Block {
	Empty(0),
	Dirt(1, true, true),
	Rock(2, false, true),
	BaseWall(3, false, true),
	Undefined(15, false, true);

	private static final HashMap<Byte, Block> typeMap;

	public static Block fromByteValue(byte c) {
		return typeMap.getOrDefault(c, Undefined);
	}

	static {
		typeMap = new HashMap<>();
		for (Block type : Block.values()) {
			typeMap.put(type.byteValue(), type);
		}
	}

	private final byte typeValue;
	private final boolean breakable;
	private final boolean obstacle;

	Block(int type) {
		this(type, false, false);
	}

	Block(int type, boolean breakable, boolean obstacle) {
		this.typeValue = (byte) type;
		this.breakable = breakable;
		this.obstacle = obstacle;
	}

	public boolean isBreakable() {
		return this.breakable;
	}

	public boolean isObstacle() {
		return this.obstacle;
	}

	public byte byteValue() {
		return this.typeValue;
	}
}

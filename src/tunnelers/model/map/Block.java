package tunnelers.model.map;

/**
 *
 * @author Stepan
 */
public enum Block {

	Breakable(Block.C_BREAKABLE),
	Tough(Block.C_TOUGH),
	Empty(Block.C_EMPTY),
	BaseWall(Block.C_BASEWALL),
	Undefined(Block.C_UNDEFINED);
	private static final char C_BREAKABLE = 'a',
			C_TOUGH = 'b',
			C_EMPTY = 'c',
			C_BASEWALL = 'd',
			C_UNDEFINED = 'z';

	public static Block fromChar(char c) {
		switch (c) {
			default:
				return Undefined;
			case C_BREAKABLE:
				return Breakable;
			case C_TOUGH:
				return Tough;
			case C_EMPTY:
				return Empty;
			case C_BASEWALL:
				return BaseWall;
		}
	}

	private final char type;

	private Block(char type) {
		this.type = type;
	}

	public boolean isBreakable() {
		return this.type == Breakable.type;
	}
}

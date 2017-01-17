package tunnelers.core.engine;

import org.junit.Test;
import static org.junit.Assert.*;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Chunk;

/**
 *
 * @author Skoro
 */
public class MapChunkParserTest {
	
	public MapChunkParserTest() {
	}

	/**
	 * Test of parseData method, of class MapChunkParser.
	 */
	@Test
	public void testParseData() {
		MapChunkParser instance = new MapChunkParser();
		
		String source = "01237";
		
		Block[] expResult = new Block[]{
			Block.Empty, Block.Breakable, Block.Tough, Block.BaseWall, Block.Undefined,
		};
		
		assertArrayEquals(expResult, instance.parseData(source));
	}

	/**
	 * Test of parse method, of class MapChunkParser.
	 */
	@Test
	public void testParse() {
		MapChunkParser instance = new MapChunkParser();
		
		Chunk chunk = new Chunk(3);
		chunk.setBlock(0, 1, Block.Empty);
		chunk.setBlock(1, 1, Block.BaseWall);
		chunk.setBlock(2, 1, Block.Tough);
		
		String expResult = "111032111";
		assertEquals(expResult, instance.parse(chunk));
	}
	
}

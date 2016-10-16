package tunnelers.core.model.map;

import generic.RNG;

/**
 *
 * @author Stepan
 */
public class MapGenerator {

	public static Chunk makeChunk(int x, int y, int chunkSize) {
		Chunk tmp = new Chunk(x, y, chunkSize);

		for (int row = 0; row < chunkSize; row++) {
			for (int col = 0; col < chunkSize; col++) {
				int val = RNG.getRandInt(100);
				tmp.chunkData[row][col] = (val < 75) ? Block.Breakable
						: (val < 95) ? Block.Tough : Block.BaseWall;
			}
		}

		return tmp;
	}

	public static Map mockMap(int chunkSize, int playerCount) {
		return new Map(chunkSize, 12, 8, playerCount);
	}
}

package temp;

import generic.RNG;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Chunk;
import tunnelers.core.model.map.ChunkType;
import tunnelers.core.model.map.Map;

/**
 *
 * @author Stepan
 */
public class MapGenerator {

	public static Map mockMap(int chunkSize, int width, int height, int playerCount) {
		Map map = new Map(chunkSize, width, height, playerCount);

		Chunk[][] tmp = new Chunk[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tmp[i][j] = MapGenerator.makeChunk(i, j, chunkSize);
			}
		}
		Chunk[] baseChunks = initPlayerBaseChunks(playerCount, tmp);

		map.initChunks(tmp, baseChunks);
		return map;
	}

	private static Chunk makeChunk(int x, int y, int chunkSize) {
		Chunk tmp = new Chunk(x, y, chunkSize);

		for (int row = 0; row < chunkSize; row++) {
			for (int col = 0; col < chunkSize; col++) {
				int val = RNG.getRandInt(100);
				Block block = (val < 75) ? Block.Breakable
						: (val < 95) ? Block.Tough : Block.BaseWall;
				tmp.setBlock(row, col, block);
			}
		}

		return tmp;
	}
	
	

	private static Chunk[] initPlayerBaseChunks(int playerCount, Chunk[][] source) {
		Chunk[] chunks = new Chunk[playerCount];
		int Xchunks = source.length, Ychunks = source[0].length;
		
		// TODO: base distance
		// TODO: terrain editing
		for (int i = 0; i < playerCount; i++) {
			Chunk currentChunk;

			do {
				int x = RNG.getRandInt(Xchunks - 2) + 1,
						y = RNG.getRandInt(Ychunks - 2) + 1;
				currentChunk = source[x][y];
			} while (currentChunk.isBase());

			currentChunk.setType(ChunkType.Base);
			chunks[i] = currentChunk;
		}

		return chunks;
	}
}

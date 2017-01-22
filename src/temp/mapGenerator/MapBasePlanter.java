package temp.mapGenerator;

import generic.RNG;
import tunnelers.core.model.entities.IntPoint;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Chunk;
import tunnelers.core.model.map.Map;

public class MapBasePlanter implements IMapGeneratorStep {

	private final RNG rng;

	public MapBasePlanter(RNG rng) {
		this.rng = rng;

	}

	@Override
	public void applyOn(Map map) {
		int playerCount = map.getPlayerCount();
		int mapWidth = map.getWidth(), mapHeight = map.getHeight();

		// TODO: base distance
		for (int i = 0; i < playerCount; i++) {
			Chunk currentChunk;
			int x, y;
			do {
				x = RNG.getRandInt(mapWidth - 2) + 1;
				y = RNG.getRandInt(mapHeight - 2) + 1;
				currentChunk = map.getChunk(x, y);
			} while (currentChunk.isBase());

			this.plantBaseOn(currentChunk, map.getChunkSize());
			map.setPlayerBaseChunk(i, new IntPoint(x, y), null);
		}

	}

	private Chunk plantBaseOn(Chunk chunk, int chunkSize) {
		int padding = 3;

		int top = padding, left = top, bottom = chunkSize - padding, right = bottom;

		for (int row = top; row <= bottom; row++) {
			chunk.setBlock(left, row, Block.BaseWall);
			chunk.setBlock(right, row, Block.BaseWall);
		}
		for (int col = left; col <= right; col++) {
			chunk.setBlock(col, top, Block.BaseWall);
			chunk.setBlock(col, bottom, Block.BaseWall);
		}

		for (int row = top + 1; row < bottom; row++) {
			for (int col = left + 1; col < right; col++) {
				chunk.setBlock(col, row, Block.Empty);
			}
		}

		int width = (chunkSize % 2) == 1 ? 7 : 6;

		int start = (chunkSize - width) / 2, end = (chunkSize + width) / 2;
		int orientation = this.rng.getInt(2);
		for (int i = start; i <= end; i++) {
			if (orientation == 0) {
				chunk.setBlock(left, i, Block.Empty);
				chunk.setBlock(right, i, Block.Empty);
			} else {
				chunk.setBlock(i, top, Block.Empty);
				chunk.setBlock(i, bottom, Block.Empty);
			}
		}

		return chunk;
	}

}

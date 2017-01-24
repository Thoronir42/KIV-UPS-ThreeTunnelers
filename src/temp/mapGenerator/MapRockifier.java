package temp.mapGenerator;

import generic.RNG;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Chunk;
import tunnelers.core.model.map.Map;

public class MapRockifier implements IMapGeneratorStep {

	private final RNG rng;

	private final int borderSmoothness;

	public MapRockifier(RNG rng, int borderSmoothness){
		this.rng = rng;
		this.borderSmoothness = borderSmoothness;
		
	}
	
	@Override
	public void applyOn(Map map) {
		int width = map.getWidth(), height = map.getHeight();
		int chunkSize = map.getChunkSize();
		
		AvgLine l1 = new AvgLine(this.borderSmoothness, chunkSize / 2);
		AvgLine l2 = new AvgLine(this.borderSmoothness, chunkSize / 2);
		
		for (int i = 0; i < width; i++) {
			applyTopLine(map.getChunk(i, 0), l1, chunkSize);
			applyBottomLine(map.getChunk(i, height - 1), l2, chunkSize);
		}
		
		l1 = new AvgLine(this.borderSmoothness, chunkSize / 2);
		l2 = new AvgLine(this.borderSmoothness, chunkSize / 2);
		
		for (int i = 0; i < height; i++) {
			applyLeftLine(map.getChunk(0, i), l1, chunkSize);
			applyRightLine(map.getChunk(width - 1, i), l2, chunkSize);
		}
		
	}

	private void applyTopLine(Chunk chunk, AvgLine line, int chunkSize) {
		for(int i = 0; i < chunkSize; i++){
			int padding = line.put(this.rng.getInt(chunkSize));
			for(int offset = 0; offset < padding; offset++){
				chunk.setBlock(i, offset, Block.Rock);
			}
		}
	}
	
	private void applyBottomLine(Chunk chunk, AvgLine line, int chunkSize) {
		for(int i = 0; i < chunkSize; i++){
			int padding = line.put(this.rng.getInt(chunkSize));
			for(int offset = 0; offset < padding; offset++){
				chunk.setBlock(i, chunkSize - offset - 1, Block.Rock);
			}
		}
	}
	

	private void applyLeftLine(Chunk chunk, AvgLine line, int chunkSize) {
		for(int i = 0; i < chunkSize; i++){
			int padding = line.put(this.rng.getInt(chunkSize));
			for(int offset = 0; offset < padding; offset++){
				chunk.setBlock(offset, i, Block.Rock);
			}
		}
	}
	
	private void applyRightLine(Chunk chunk, AvgLine line, int chunkSize) {
		for(int i = 0; i < chunkSize; i++){
			int padding = line.put(this.rng.getInt(chunkSize));
			for(int offset = 0; offset < padding; offset++){
				chunk.setBlock(chunkSize - offset - 1, i, Block.Rock);
			}
		}
	}

}

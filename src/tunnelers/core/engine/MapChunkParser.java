package tunnelers.core.engine;

import generic.SimpleScanner;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Chunk;

/**
 *
 * @author Skoro
 */
public class MapChunkParser {

	public MapChunkParser() {
	}

	public Block[] parseData(String source) {
		Block[] data = new Block[source.length()];
		for (int i = 0; i < data.length; i++) {
			byte value = Byte.parseByte(source.substring(i, i + 1), 16);
			data[i] = Block.fromByteValue(value);
		}

		return data;
	}

	public String parse(Chunk chunk) {
		int chunkSize = chunk.getSize();
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < chunkSize; y++) {
			for (int x = 0; x < chunkSize; x++) {
				sb.append(String.format("%01X", chunk.getBlock(x, y).byteValue()));
			}
		}

		return sb.toString();
	}
}

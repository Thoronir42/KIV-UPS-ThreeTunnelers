package tunnelers.core.engine;

import generic.SimpleScanner;
import generic.SimpleScannerException;
import tunnelers.core.model.map.Block;
import tunnelers.core.model.map.Chunk;

public class MapChunkParser {

	MapChunkParser() {
	}

	public Block[] parseData(SimpleScanner source) throws SimpleScannerException {
		Block[] data = new Block[source.remainingLength()];
		for (int i = 0; i < data.length; i++) {
			try {
				byte value = Byte.parseByte(source.read(1), 16);
				data[i] = Block.fromByteValue(value);
			} catch (NumberFormatException ex) {
				data[i] = Block.Undefined;
			}
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

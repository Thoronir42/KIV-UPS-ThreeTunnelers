package tunnelers.Game.Map;

import javafx.geometry.Point2D;
import tunnelers.Game.Frame.Player;
import tunnelers.Game.Frame.Tank;
import tunnelers.Configuration.Settings;

/**
 *
 * @author Stepan
 */
public class MapGenerator {

	public static Chunk makeChunk(int x, int y, int chunkSize) {
		Chunk tmp = new Chunk(x, y, chunkSize);

		for (int row = 0; row < chunkSize; row++) {
			for (int col = 0; col < chunkSize; col++) {
				int val = Settings.getRandInt(100);
				tmp.chunkData[row][col] = (val < 75) ? Block.Breakable
						: (val < 95) ? Block.Tough : Block.BaseWall;
			}
		}

		return tmp;
	}

	public static TunnelMap mockMap(Player[] players) {
		TunnelMap map = new TunnelMap(Settings.MOCK_CHUNK_SIZE, 12, 8);
		
		for (Player p : players) {
			Point2D baseCenter = map.getFreeBaseSpot(p);
			Tank tank = new Tank(p, baseCenter);
			p.setTank(tank);
		}
		
		return map;
	}
}

package tunnelers.core.gameRoom;

import tunnelers.core.player.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Point2D;
import tunnelers.core.model.map.Map;
import tunnelers.core.model.entities.Tank;

/**
 *
 * @author Stepan
 */
public class GameContainer {

	private Warzone warzone;
	private List<Player> players;

	public GameContainer(Player[] players) {
		this(Arrays.asList(players));
	}

	public GameContainer(int expectedPlayerCount) {
		this(new ArrayList<>(expectedPlayerCount));
	}

	public GameContainer(List<Player> players) {
		this.players = players;
	}

	public void initWarzone(Map map) {
		ArrayList<Tank> tanks = new ArrayList<>(players.size());
		
		int i = 0;
		for (Player p : players) {
			Point2D baseCenter = map.assignBase(i++, p);
			Tank tank = new Tank(p, baseCenter);
			p.setTank(tank);
			tanks.add(tank);
		}
		
		this.warzone = new Warzone(tanks, map);
	}

	public int getPlayerCount() {
		return players.size();
	}

	public Player getPlayer(int playerId) {
		for (Player p : this.players) {
			if (p.getID() == playerId) {
				return p;
			}
		}
		return null;
	}
	
	public Warzone getWarzone() {
		return this.warzone;
	}
	
	public List<Player> getPlayers() {
		return this.players;
	}
}

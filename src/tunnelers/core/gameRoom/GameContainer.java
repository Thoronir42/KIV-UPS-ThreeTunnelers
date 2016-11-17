package tunnelers.core.gameRoom;

import tunnelers.core.player.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Point2D;
import tunnelers.core.model.map.Map;
import tunnelers.core.model.entities.Tank;
import tunnelers.network.NetClient;

/**
 *
 * @author Stepan
 */
public class GameContainer {

	private Warzone warzone;
	private final Player[] players;

	public GameContainer(int capacity) {
		this.players = new Player[capacity];
	}

	public void initWarzone(Map map) {
		ArrayList<Tank> tanks = new ArrayList<>(players.length);
		
		int i = 0;
		for (Player p : players) {
			Point2D baseCenter = map.assignBase(i++, p);
			Tank tank = new Tank(p, baseCenter);
			p.setTank(tank);
			tanks.add(tank);
		}
		
		this.warzone = new Warzone(tanks, map);
	}

	public int getCapacity(){
		return this.players.length;
	}
	
	public int getPlayerCount() {
		int count = 0;
		for(Player p : this.players){
			count++;
		}
		return count;
	}

	public Player getPlayer(int playerId) {
		return this.players[playerId];
	}
	
	public void setPlayer(int playerId, Player player){
		this.players[playerId] = player;
	}
	
	public Warzone getWarzone() {
		return this.warzone;
	}
	
	public List<Player> getPlayers() {
		return Arrays.asList(this.players);
	}
}

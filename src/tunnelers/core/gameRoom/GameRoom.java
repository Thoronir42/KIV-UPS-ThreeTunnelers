package tunnelers.core.gameRoom;

import tunnelers.core.player.Player;
import java.util.Arrays;
import javafx.geometry.Point2D;
import tunnelers.core.chat.Chat;
import tunnelers.core.model.map.Map;
import tunnelers.core.model.entities.Tank;
import tunnelers.network.NetClient;


public class GameRoom {
	
	private final NetClient[] clients;
	private final Player[] players;
	
	private final Chat chat;
	
	private Warzone warzone;

	public GameRoom(int capacity, int chatCapacity) {
		this.clients = new NetClient[capacity];
		this.players = new Player[capacity];
		this.chat = new Chat(chatCapacity);
	}

	public void initWarzone(Map map) {
		Tank[] tanks = new Tank[players.length];
		
		
		for (int i = 0; i < players.length; i++) {
			Player p  = players[i];
			if(p == null){
				continue;
			}
			Point2D baseCenter = map.assignBase(i, p);
			Tank tank = new Tank(p, baseCenter);
			p.setTank(tank);
			tanks[i] = tank;
		}
		
		this.warzone = new Warzone(tanks, map);
	}

	public int getCapacity(){
		return this.players.length;
	}
	
	public int getPlayerCount() {
		int count = 0;
		for(Player p : this.players){
			if(p != null){
				count++;
			}
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
	
	public Player[] getPlayers() {
		return Arrays.copyOf(this.players, this.players.length);
	}

	public Chat getChat() {
		return this.chat;
	}
}

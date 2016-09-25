package tunnelers.model;

import tunnelers.model.player.APlayer;
import tunnelers.model.player.PlayerLocal;
import tunnelers.model.player.PlayerRemote;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import tunnelers.model.map.Zone;
import tunnelers.Game.ControlSchemeManager;
import tunnelers.Game.IO.ControlScheme;
import tunnelers.model.map.MapGenerator;
import tunnelers.Settings.Settings;

/**
 *
 * @author Stepan
 */
public class GameContainer {

	public static GameContainer mockContainer(ControlSchemeManager controlSchemeManager, String localName) {
		APlayer[] players = new APlayer[]{
			new PlayerLocal(47, Settings.getRandInt(Settings.PLAYER_COLORS.length), localName),
			new PlayerLocal(53, Settings.getRandInt(Settings.PLAYER_COLORS.length), "Jouda"),
			new PlayerRemote(12, Settings.getRandInt(Settings.PLAYER_COLORS.length), "Frederick"),
		};
		Zone zone = MapGenerator.mockMap(players);
		GameContainer c = new GameContainer(players);
		
		c.initWarzone(zone);
		
		byte[] controlSchemeIDs = ControlSchemeManager.getKeyboardLayoutIDs();
		for(byte i = 0; i < controlSchemeIDs.length; i++){
			ControlScheme.Keyboard keyboardScheme= controlSchemeManager.getKeyboardScheme(i);
			keyboardScheme.setPlayerID(players[i].getID());
			//System.out.format("sch: %s - pid: %d\n", keyboardScheme.toString(), keyboardScheme.getPlayerID());
		}
		
		
		return c;
	}
	
	private Warzone warzone;
	private List<APlayer> players;
	
	public GameContainer(APlayer[] players){
		this(Arrays.asList(players));
	}
	
	public GameContainer(int expectedPlayerCount){
		this(new ArrayList<>(expectedPlayerCount));
	}
	
	
	public GameContainer(List<APlayer> players) {
		this.players = players;
	}
	
	public void initWarzone(Zone zone){
		if(zone != null){
			System.err.println("Warzone had already been set");
		}
		this.warzone = new Warzone(players, zone);
	}
	
	public Warzone getWarzone(){
		return this.warzone;
	}

	public int getPlayerCount() {
		return players.size();
	}

	public List<APlayer> getPlayers() {
		return this.players;
	}
	

	/**
	 * 
	 * @return 
	 * @deprecated use Warzone in stead
	 */
	public Zone getMap() {
		return this.getWarzone().getMap();
	}

	public APlayer getPlayer(int playerId) {
		for (APlayer p : this.players) {
			if (p.getID() == playerId) {
				return p;
			}
		}
		return null;
	}
	
	public APlayer getLocalPlayer() {
		for (APlayer p : this.players) {
			return p;
		}
		return null;
	}

}

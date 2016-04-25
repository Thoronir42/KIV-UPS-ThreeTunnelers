package tunnelers.Menu.ServerList;

/**
 *
 * @author Stepan
 */
public interface GRTVItem {
	Difficulty getDIfficulty();
}

class GRTVRoot implements GRTVItem{

	@Override
	public Difficulty getDIfficulty() {
		return null;
	}
	
	@Override
	public String toString(){
		return "Seznam aktivních her";
	}
	
}

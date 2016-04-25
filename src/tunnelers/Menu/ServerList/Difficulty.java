package tunnelers.Menu.ServerList;

/**
 *
 * @author Stepan
 */
public enum Difficulty implements GRTVItem{
	Easy("Lehká", 1),
	Medium("Střední", 2),
	Hard("Těžká", 3),
	Unspecified("Nespecifikována", 0);

	private final String label;
	private final int value;

	private Difficulty(String label, int value){
		this.label = label;
		this.value = value;	
	}

	@Override
	public String toString(){
		return "Obtžnost " + label;
	}

	public int intValue(){
		return value;
	}

	@Override
	public Difficulty getDIfficulty() {
		return this;
	}
	
	

}

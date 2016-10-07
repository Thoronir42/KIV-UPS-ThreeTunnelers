package generic;

/**
 *
 * @author Stepan
 */
public class Port {
	public static final int MAX_PORT = 65535;
	public static final int MIN_PORT = 0;
	
	public static final int TUNNELER_DEFAULT_PORT = 14916;
	
	
	public static final boolean validate(int value){
		return validate(value, false);
	}
	
	public static final boolean validate(int value, boolean strict){
		if(value < MIN_PORT || value > MAX_PORT){
			if(strict){
				throw new IllegalArgumentException(value + " is not a valid "
						+ "port value. <0; 65535> expected");
			}
			
			return false;
		}
		
		return true;
	}
}

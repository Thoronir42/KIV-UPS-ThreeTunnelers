package tunnelers.core.io;

import generic.RNG;

/**
 *
 * @author Stepan
 */
public class RemoteControlScheme extends AControls{
	
	public static final RNG RNG = new RNG(37);
	
	public RemoteControlScheme(byte schemeId) {
		super(schemeId);
	}
	
}

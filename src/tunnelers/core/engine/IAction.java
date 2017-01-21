package tunnelers.core.engine;

import generic.SimpleScanner;
import generic.SimpleScannerException;

/**
 *
 * @author Skoro
 */
public interface IAction {
	public boolean execute(SimpleScanner sc) throws SimpleScannerException;
}

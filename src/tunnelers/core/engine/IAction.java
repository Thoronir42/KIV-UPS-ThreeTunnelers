package tunnelers.core.engine;

import generic.SimpleScanner;
import generic.SimpleScannerException;

public interface IAction {
	boolean execute(SimpleScanner sc) throws SimpleScannerException;
}

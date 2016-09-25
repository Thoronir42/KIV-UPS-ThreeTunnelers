package generic;

/**
 *
 * @author Stepan Sevcik, kiwi A13B0433P
 * @param <Type> type specificator
 * @deprecated This is a weird way of simulating events
 */
public class BackPasser<Type> implements Runnable {

	private Type content;

	public Type get() {
		return content;
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Command hasn't been passed: no uzuý");
	}

	public void pass(Type content) {
		this.content = content;
		this.run();
	}

}

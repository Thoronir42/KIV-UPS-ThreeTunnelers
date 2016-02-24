package generic;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 *
 * @author Stepan
 * @param <Type>
 */
public class CyclicArray<Type> implements Iterable<Type> {

	private final Type[] array;
	protected int top = 0;

	public CyclicArray(Class<Type> c, int n) {
		final Type[] a = (Type[]) Array.newInstance(c, n + 1);
		this.array = a;
	}

	public void add(Type item) {
		array[top] = item;
		if (++top >= array.length) {
			top = 0;
		}
	}

	public Type get(int k) throws ArrayIndexOutOfBoundsException {
		if (k < 0 || k >= array.length) {
			throw new ArrayIndexOutOfBoundsException(k);
		}
		return array[k];
	}

	public int size() {
		return this.array.length;
	}

	@Override
	public CyclicIterator<Type> iterator() {
		return new CyclicIterator(this);
	}

	class CyclicIterator<Type> implements Iterator {

		private CyclicArray<Type> array;
		private final int startIndex;
		private int curIndex;

		public CyclicIterator(CyclicArray<Type> array) {
			this.array = array;
			this.startIndex = this.curIndex = array.top;
		}

		@Override
		public boolean hasNext() {
			int i = nextIndex();
			if (i == startIndex) {
				return false;
			}
			return array.get(i) != null;
		}

		@Override
		public Type next() {
			this.curIndex = nextIndex();
			return array.get(this.curIndex);
		}

		private int nextIndex() {
			int val = this.curIndex - 1;
			if (val < 0) {
				val = this.array.size() - 1;
			}
			return val;
		}
	}
}

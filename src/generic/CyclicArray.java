package generic;

import java.lang.reflect.Array;
import java.util.Iterator;

public class CyclicArray<Type> implements Iterable<Type> {

	private final Type[] array;
	protected int top;

	public CyclicArray(Class<Type> c, int length) {
		this.top = 0;

		@SuppressWarnings("unchecked") final Type[] array = (Type[]) Array.newInstance(c, length);
		this.array = array;
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
	public CyclicIterator iterator() {
		return new CyclicIterator(this);
	}

	public class CyclicIterator implements Iterator<Type> {

		private final CyclicArray<Type> array;
		private final int startIndex;
		private int curIndex;

		CyclicIterator(CyclicArray<Type> array) {
			this.array = array;
			this.startIndex = this.curIndex = array.top;
		}

		@Override
		public boolean hasNext() {
			int i = nextIndex();
			return i != startIndex && array.get(i) != null;
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

package temp.mapGenerator;

public class AvgLine {

	private final int[] members;

	public AvgLine(int members) {
		this(members, 0);
	}

	AvgLine(int members, int initialValue) {
		this.members = new int[members];

		for (int i = 0; i < members; i++) {
			this.members[i] = initialValue;
		}
	}

	public int put(int value) {
		System.arraycopy(this.members, 1, this.members, 0, this.members.length - 1);

		this.members[this.members.length - 1] = value;

		return this.getValue();
	}

	public int getValue() {
		int sum = 0;

		for (int n : this.members) {
			sum += n;
		}

		return sum / this.members.length;
	}
}

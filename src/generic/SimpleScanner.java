package generic;

public class SimpleScanner {

	public static final int RADIX_DECIMAL = 10;
	public static final int RADIX_HEXADECIMAL = 16;

	private final int parse_radix;

	private String source;
	private int pointer;

	public SimpleScanner() {
		this(RADIX_DECIMAL, "");
	}

	public SimpleScanner(int radix) {
		this(radix, "");
	}

	public SimpleScanner(int radix, String source) {
		this.parse_radix = radix;
		this.setSourceString(source);
	}

	public void setSourceString(String source) {
		this.source = source;
		this.pointer = 0;
	}

	public int remainingLength() {
		return source.length() - pointer;
	}

	public short nextByte() throws NumberFormatException, SimpleScannerException {
		return Short.parseShort(read(2), parse_radix);
	}

	public short nextShort() throws NumberFormatException, SimpleScannerException {
		return Short.parseShort(read(4), parse_radix);
	}

	public int nextInt() throws NumberFormatException, SimpleScannerException {
		return Integer.parseInt(read(8), parse_radix);
	}

	public long nextLong() throws NumberFormatException, SimpleScannerException {
		return Long.parseLong(read(16), parse_radix);
	}

	public String read(int n) throws SimpleScannerException {
		if (remainingLength() < n) {
			throw new SimpleScannerException(String.format("Attempted to read %d chars from processed data", n));
		}

		String data = source.substring(pointer, pointer + n);
		pointer += n;
		return data;
	}

	public String readToEnd() {
		String data = source.substring(pointer);
		pointer = source.length();

		return data;
	}
}

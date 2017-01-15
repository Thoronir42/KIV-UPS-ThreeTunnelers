package generic;

/**
 *
 * @author Stepan
 */
public class SimpleScanner {

	public static final int RADIX_DECIMAL = 10;
	public static final int RADIX_HEXADECIMAL = 16;

	private final int parse_radix;
	
	private String source;
	private int pointer;

	public SimpleScanner(){
		this("", RADIX_DECIMAL);
	}
	
	public SimpleScanner(String source) {
		this(source, RADIX_DECIMAL);
	}
	
	public SimpleScanner(int radix){
		this("", radix);
	}

	public SimpleScanner(String source, int radix) {
		this.parse_radix = radix;
		this.setSourceString(source);
	}
	
	public void setSourceString(String source){
		this.source = source;
		this.pointer = 0;
	}

	public int remainingLength() {
		return source.length() - pointer;
	}

	public short nextByte() {
		return Short.parseShort(read(2), parse_radix);
	}

	public short nextShort() {
		return Short.parseShort(read(4), parse_radix);
	}

	public int nextInt() {
		return Integer.parseInt(read(8), parse_radix);
	}

	public String read(int n) {
		if (remainingLength() <= 0) {
			throw new StringIndexOutOfBoundsException(String.format("Attemoted to read %d chars from processed data", n));
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

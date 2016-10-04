package tunnelers.network.codec;

/**
 *
 * @author Stepan
 */
public class NoCodec implements ICodec {
	
	@Override
	public String decode(String cipher) {
		return cipher;
	}

	@Override
	public String encode(String plainText) {
		return plainText;
	}
}

package tunnelers.network.codec;

/**
 *
 * @author Stepan
 */
public interface ICodec {
	
	String decode(String cipher);

	String encode(String plainText);
}

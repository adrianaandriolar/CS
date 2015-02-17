import FormatIO.EofX;
import FormatIO.FileIn;
import FormatIO.FileOut;

/**
 * @since 16/02/2015
 * @author antonbelev
 * Known plain text attack - brute force algorithm
 */
public class KPT {
	
	private static String plainTextFristBlock = "0x446f";
	private static String cipherTextFirstBlock = "0xd1b2";
	private static String plainTextFirstBlockString = "Do";
	private static String cipherTextFirstBlockStringDecrypted = "";


	public static void main(String[] args) {
		for (int key = 0; key < Math.pow(2, 16); key++){
			cipherTextFirstBlockStringDecrypted = decrypt(intToHexString(key), cipherTextFirstBlock);
			if (cipherTextFirstBlockStringDecrypted.equals(plainTextFristBlock))
				System.out.println("Potential key decimal - " + key 
						+ " hex - " + intToHexString(key));
		}
	}
	
	private static String intToHexString(int i){
		return "0x" + String.format("%04x", (0xFFFF & i));
	}
	
	private static String decrypt(String hexString, String cipherBlock){
		int	key = Hex16.convert(hexString);
		for (;;)
		{
			int	c = Hex16.convert(cipherBlock);
			int	p = Coder.decrypt(key, c);
			String	out = String.format("0x%04x", p);
			return out;
		}
	}

}

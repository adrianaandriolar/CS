
/**
 * @since 16/02/2015
 * @author antonbelev
 * Known plain text attack - brute force algorithm
 */
public class KPT {
	
	private static String plainTextFristBlock = "0x446f";
	private static String cipherTextFirstBlock = "0xd1b2";
	private static String cipherTextFirstBlockStringDecrypted = "";


	public static void main(String[] args) {
		for (int key = 0; key < Math.pow(2, 16); key++){
			cipherTextFirstBlockStringDecrypted = decrypt(intToHexString(key), cipherTextFirstBlock);
			if (cipherTextFirstBlockStringDecrypted.equals(plainTextFristBlock)){
				System.out.println("Potential key decimal - " + key 
						+ " hex - " + intToHexString(key));
				DecryptCipher.decrypt("c1", intToHexString(key));
			}
		}
	}
	
	private static String intToHexString(int i){
		return String.format("0x%04x", i);
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

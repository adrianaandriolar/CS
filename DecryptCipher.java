
public abstract class DecryptCipher {
	/**
	 * 
	 * @param cipherFile File name of the ciphertext file (excluding .txt)
	 * @param keyHexString String representation of hex key
	 */
	public static void decrypt(String cipherFile, String keyHexString){
		String fileIn = cipherFile + ".txt";
		String fileOut = cipherFile + "_decrypted_" + keyHexString + ".txt";
		//String keyHexString = "0xfd1f";
		String[] arguments = {fileIn, fileOut, keyHexString};
		DecryptAllBlocks.main(arguments);
		String[] arguments2 = {cipherFile + "_decrypted_" + keyHexString };
		Block2Text.main(arguments2);
		System.out.println("Done! Decrypted message is stored in " + cipherFile + "_decrypted_" + keyHexString + ".txt");
	}

}

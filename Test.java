
public class Test {

	public static void main(String[] args) {
		String fileIn = "c1.txt";
		String fileOut = "c1_decrypted_2.txt";
		String keyHexString = "0xb4db";
		String[] arguments = {fileIn, fileOut, keyHexString};
		DecryptAllBlocks.main(arguments);
		String[] arguments2 = {"c1_decrypted_2"};
		//System.out.println(keyHexString);
		Block2Text.main(arguments2);
	}

}

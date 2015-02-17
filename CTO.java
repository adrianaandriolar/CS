import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 16/02/2015
 * @author antonbelev
 * Cipher Text Only Attack
 */
public class CTO {
	
	public static void main(String[] args) throws IOException {
		for (int key = 0; key < Math.pow(2, 16); key++){
			String fileIn = "c2.txt";
			String fileOut = "c2_decrypted.txt";
			
			String keyHexString = intToHexString(key);
			if(key%1000 == 0)
				System.out.println(keyHexString);
			String[] arguments = {fileIn, fileOut, keyHexString};
			DecryptAllBlocks.main(arguments);
			String[] arguments2 = {"c2_decrypted"};
			//System.out.println(keyHexString);
			Block2Text.main(arguments2);
			if (checkDecryptedTextForEnglishChars("c2_decrypted_t.txt"))
				System.out.println("Potential key decimal - " + key 
						+ " hex - " + keyHexString);
		}
	}
	
	private static boolean checkDecryptedTextForEnglishChars(String fileName) throws IOException {
		//Scanner scan = new Scanner(new File(fileName));
		File f = new File(fileName);
	    InputStream targetStream = new FileInputStream(f);
	    int nextByte = targetStream.read();
		while (nextByte != -1 ){
			//String line = scan.nextLine();
			if (!isLatinLetter(nextByte)){
				//scan.close();
				//System.out.print(line);
				targetStream.close();
				return false;
			}
			nextByte = targetStream.read();
//			if (containsSpecialChars(line)){
//				scan.close();
//				System.out.print(line);
//				return false;
//			}
		}
		targetStream.close();
		//scan.close();
		return true;
	}
	
	

	private static boolean containsSpecialChars(String string)
	{
		Pattern p = Pattern.compile("[a-zA-Z_0-9 ]*");
	    Matcher m = p.matcher(string);
	    return m.find();
	}
	
	public static boolean isLatinLetter(int c) {
	    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'
	    		|| c == ' ' || c == '.' || c == ',' || c == '\'' || c == '!' || c == '?');
	}

	private static String intToHexString(int i){
		return String.format("0x%04x", i);
	}
}

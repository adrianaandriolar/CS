import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 
 * @author antonbelev
 *
 */
public class TMT2 {

	private static final String C3_TXT = "c3.txt";

	public static void main(String[] args) throws FileNotFoundException {
		Table t = TMT1.loadTable();	
		ArrayList<String> cipherBlocks = DecryptAllBlocks.getCipherBlocks(C3_TXT);		
		for (String cipherBlock : cipherBlocks){
			int intKey = t.find(Hex16.convert(cipherBlock));
			if(intKey != -1){
				System.out.println("Key found. Decimal value - " + intKey 
						+ " Hex value - " + TMT1.intToHexString(intKey));
				ArrayList<String> dblocks = DecryptAllBlocks.decrypt(cipherBlocks, TMT1.intToHexString(intKey));
				String text = Block2Text.block2Text(dblocks);
				System.out.println("Decrypted text:\n" + text);
				break;
			}
		}
	}
}

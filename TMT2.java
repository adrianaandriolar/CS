import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 
 * @author Anton Belev 1103816b
 * Time Memory Tradeoff 2. This class uses the TMT table generated from TMT1 and finds the encrypted message
 */
public class TMT2 {
	private static Table t;
	private static int plainTextBlock;
	private static int cipherBlock;
	private static int key;
	private static int N = (int) Math.pow(2, 8); // Since L * N should be equal to 2^16 (number of keys).
	private static int L = (int) Math.pow(2, 8); // Assume L = N = 2^8
	
	public static void main(String[] args) throws FileNotFoundException {
		t = Utils.loadTable();
		key = -1;
		ArrayList<String> cipherBlocks = Utils.getCipherBlocks(Utils.C3_TXT);
		plainTextBlock = Hex16.convert(Utils.getFirstPlainTextBlock(Utils.P3_TXT));
		cipherBlock = Hex16.convert(cipherBlocks.get(0));
		
		
		if (t.find(cipherBlock) != -1){
			key = tableContainsKey(cipherBlock);
		}
		else{
			int x2 = cipherBlock;
			int x1 = Coder.encrypt(x2, plainTextBlock);
			for (int j = 0; j < N; j++){		
				x2 = Coder.encrypt(x1, plainTextBlock);
				if(t.find(x2) != -1){
					key = tableContainsKey(x2);
					if(key == -1)
						break;
				}
				x1 = Coder.encrypt(x2, plainTextBlock);
				if(t.find(x1) != -1){
					key = tableContainsKey(x1);
					if(key == -1)
						break;
				}
			}
		}
		if(key == -1)
			System.out.println("Key not found!");
		else{
			ArrayList<String> dblocks = Utils.decrypt(cipherBlocks, Utils.intToHexString(key));
			String text = Utils.block2Text(dblocks);
			System.out.println("Decrypted text:\n" + text);
		}
	}
	
	public static int tableContainsKey(int cipher){
		int x2 = t.find(cipher); //thats the initial x0
		int x1 = Coder.encrypt(x2, plainTextBlock);
		for (int i = 0; i < L; i++){
			x2 = Coder.encrypt(x1, plainTextBlock);
			if(x2==cipherBlock)
				return x1;
			x1 = Coder.encrypt(x2, plainTextBlock);
			if(x1==cipherBlock)
				return x2;
		}
		return -1;
	}
}
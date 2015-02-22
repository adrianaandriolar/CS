import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 
 * @author Anton Belev 1103816b
 * Time Memory Tradeoff 2. This class uses the TMT table generated from TMT1 and finds the encrypted message
 */
public class TMT2 {

	private static final String C3_TXT = "c3.txt";
	private static Table t;
	private static int plainTextBlock;
	private static int cipherBlock;
	private static int key;
	
	public static void main(String[] args) throws FileNotFoundException {
		t = TMT1.loadTable();
		key = -1;
		ArrayList<String> cipherBlocks = DecryptAllBlocks.getCipherBlocks(C3_TXT);
		plainTextBlock = Hex16.convert(TMT1.getFirstPlainTextBlock());
		cipherBlock = Hex16.convert(cipherBlocks.get(0));		
		
		if (t.find(cipherBlock) != -1){
			key = tableHasKey(cipherBlock);
		}
		else{
			int x2 = cipherBlock;
			int x1 = Coder.encrypt(x2, plainTextBlock);
			for (int j = 0; j < TMT1.N; j++){		
				x2 = Coder.encrypt(x1, plainTextBlock);
				if(t.find(x2) != -1){
					key = tableHasKey(x2);
					if(key == -1)
						break;
				}
				x1 = Coder.encrypt(x2, plainTextBlock);
				if(t.find(x1) != -1){
					key = tableHasKey(x1);
					if(key == -1)
						break;
				}
			}
		}
		if(key == -1)
			System.out.println("Key not found!");
		else{
			ArrayList<String> dblocks = DecryptAllBlocks.decrypt(cipherBlocks, TMT1.intToHexString(key));
			String text = Block2Text.block2Text(dblocks);
			System.out.println("Decrypted text:\n" + text);
		}
	}
	
	public static int tableHasKey(int cipher){
		int x2 = t.find(cipher); //thats the initial x0
		int x1 = Coder.encrypt(x2, plainTextBlock);
		for (int i = 0; i < TMT1.L; i++){
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
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 
 * @author Anton Belev 1103816b
 * TMT1 is used to populate the Time Memory Tradeoff table
 */
public class TMT1 {
	
	
	private static int N = (int) Math.pow(2, 8); // Since L * N should be equal to 2^16 (number of keys).
	private static int L = (int) Math.pow(2, 8); // Assume L = N = 2^8
	private static Map<Integer, Integer> myTable = new HashMap<Integer, Integer>();

	private static void setUpTable() throws FileNotFoundException{
		int plainTextBlock = Hex16.convert(Utils.getFirstPlainTextBlock(Utils.P3_TXT));
		Random rn = new Random();
		for (int i = 0; i < N; i++){
			int x0 = rn.nextInt((int) Math.pow(2, 16));
			int x1 = Coder.encrypt(x0, plainTextBlock);
			int x2 = Coder.encrypt(x1, plainTextBlock);
			for(int j = 0; j < L; j++){
				x2 = Coder.encrypt(x1, plainTextBlock);
				x1 = Coder.encrypt(x2, plainTextBlock);
			}
			myTable.put(x2, x0);
		}
		Utils.writeTableToFile(myTable);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		TMT1.setUpTable();
		System.out.println("Table file created successfully. File name - " + Utils.TABLE_TXT);
	}
}

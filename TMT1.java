import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 
 * @author 1103816b
 * TMT1 is used to populate the Time Memory Tradeoff table
 */
public class TMT1 {
	
	private static final String P3_TXT = "p3.txt";

	public static void setUpTable() throws FileNotFoundException{
		Table t = new Table();
		String plainTextBlock = getFirstPlainTextBlock();
		for (int key = 0; key < Math.pow(2, 16); key++){
			String keyHexString = intToHexString(key);
			String cipherBlock = EncryptAllBlocks.encrypt(plainTextBlock, keyHexString);
			t.add(Hex16.convert(cipherBlock), Hex16.convert(keyHexString));
		}
		t.writeTableToFile();
	}
	
	private static String getFirstPlainTextBlock() throws FileNotFoundException {
		Scanner s = new Scanner(new File(P3_TXT));
		String block = "";
		while (s.hasNextLine()){
			block = s.nextLine();
			break;
		}
		s.close();
		return block;
	}
	
	public static String intToHexString(int i){
		return String.format("0x%04x", i);
	}

	public static Table loadTable() throws FileNotFoundException {
		Scanner s = new Scanner(new File(Table.TABLE_TXT));
		Table t = new Table();
		while (s.hasNextLine()){
			String[] line = s.nextLine().split(" ");
			t.add(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
		}
		s.close();
		return t;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		TMT1.setUpTable();
		System.out.println("Table file created successfully. File name - " + Table.TABLE_TXT);
	}
}

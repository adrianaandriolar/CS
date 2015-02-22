import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author 1103816b
 * TMT1 is used to populate the Time Memory Tradeoff table
 */
public class TMT1 {
	
	public static final String P3_TXT = "p3.txt";
	public static int N = (int) Math.pow(2, 8);
	public static int L = (int) Math.pow(2, 8);

	public static void setUpTable() throws FileNotFoundException{
		Table t = new Table();
		int plainTextBlock = Hex16.convert(getFirstPlainTextBlock());
		Random rn = new Random();
		for (int i = 0; i < N; i++){
			int x0 = rn.nextInt((int) Math.pow(2, 16));
			int x1 = Coder.encrypt(x0, plainTextBlock);
			int x2 = Coder.encrypt(x1, plainTextBlock);
			for(int j = 0; j < L; j++){
				x2 = Coder.encrypt(x1, plainTextBlock);
				x1 = Coder.encrypt(x2, plainTextBlock);
			}
			t.add(x2, x0);
		}
		t.writeTableToFile();
	}
	
	public static String getFirstPlainTextBlock() throws FileNotFoundException {
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

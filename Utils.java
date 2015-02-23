import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import FormatIO.EofX;
import FormatIO.FileIn;
import FormatIO.FileOut;

/**
 * 
 * @author Anton Belev 1103816b 
 * General methods used widely in other classes
 */
public abstract class Utils {
	public static final String TABLE_TXT = "table.txt";
	public static final String P3_TXT = "p3.txt";
	public static final String C3_TXT = "c3.txt";
	
	/**
	 * 
	 * @param keyString
	 * @param fileIn
	 * @return List of hex Strings with the decrypted blocks using the keyString as a key
	 */
	public static ArrayList<String> decrypt(String keyString, String fileIn) {
		ArrayList<String> decryptedBlocks = new ArrayList<String>();
		FileIn fin = new FileIn(fileIn);
		int key = Hex16.convert(keyString);
		try {
			for (;;) {
				String s = fin.readWord();
				int c = Hex16.convert(s);
				int p = Coder.decrypt(key, c);
				String out = String.format("0x%04x", p);
				decryptedBlocks.add(out);
			}
		} catch (EofX x) {
		}
		return decryptedBlocks;
	}

	/**
	 * 
	 * @param cipherBlocks
	 * @param keyString
	 * @return Decrypts the list of Hex cipherBlock Strings, using keyString and returns array of decrypted hex strings 
	 */
	public static ArrayList<String> decrypt(ArrayList<String> cipherBlocks,
			String keyString) {
		ArrayList<String> decryptedBlocks = new ArrayList<String>();
		int key = Hex16.convert(keyString);
		for (String cipherBlock : cipherBlocks) {
			int c = Hex16.convert(cipherBlock);
			int p = Coder.decrypt(key, c);
			String out = String.format("0x%04x", p);
			decryptedBlocks.add(out);
		}
		return decryptedBlocks;
	}

	/**
	 * 
	 * @param fileIn
	 * @return Reads from ciphertext file and returns list of hex cipher text blocks
	 * @throws FileNotFoundException
	 */
	public static ArrayList<String> getCipherBlocks(String fileIn)
			throws FileNotFoundException {
		ArrayList<String> cipherBlocks = new ArrayList<String>();
		Scanner s = new Scanner(new File(fileIn));
		while (s.hasNextLine()) {
			cipherBlocks.add(s.nextLine());
		}
		s.close();
		return cipherBlocks;
	}
	
	/**
	 * 
	 * @param blocks
	 * @return Returns pure text string using array of decrypted hex blocks
	 */
	public static String block2Text(ArrayList<String> blocks){
		String text = "";
		for (String block : blocks)
		{
			int	i = Hex16.convert(block);
			int	c0 = i / 256;
			int	c1 = i % 256;
			text += (char)c0;
			if (c1 != 0)
			text +=(char)c1;

		}
		return text;
	}
	
	/**
	 * 
	 * @return Loads the content of table.txt in a Table objects and returns it.
	 * @throws FileNotFoundException
	 */
	public static Table loadTable() throws FileNotFoundException {
		Scanner s = new Scanner(new File(TABLE_TXT));
		Table t = new Table();
		while (s.hasNextLine()){
			String[] line = s.nextLine().split(" ");
			t.add(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
		}
		s.close();
		return t;
	}
	
	/**
	 * 
	 * @param i
	 * @return converts integer value i to hex String in the format 0x%04x
	 */
	public static String intToHexString(int i){
		return String.format("0x%04x", i);
	}
	
	/**
	 * 
	 * @param plainTextFile
	 * @return Using the plainTextFile - reads just the first plain text hex block and returns it as a String
	 * @throws FileNotFoundException
	 */
	public static String getFirstPlainTextBlock(String plainTextFile) throws FileNotFoundException {
		Scanner s = new Scanner(new File(plainTextFile));
		String block = "";
		while (s.hasNextLine()){
			block = s.nextLine();
			break;
		}
		s.close();
		return block;
	}
	
	/**
	 * @return Void function, writing the provided Map<Integer, Integer> in table.txt
	 * @param t
	 */
	public static void writeTableToFile(Map<Integer, Integer> t){
		FileOut f = new FileOut(TABLE_TXT);
		Set<Integer> keySet = t.keySet();
		Iterator<Integer> it = keySet.iterator();
		while (it.hasNext()){
			Integer key = it.next();
			Integer data = (Integer) t.get(key);
			f.println(key + " " + data);
		}			
	}
}

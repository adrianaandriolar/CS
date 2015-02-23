/**
 * Decrypts a file containing blocks as hex numbers
 * Output is another file containing blocks
 * (c) Ron Poet
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import FormatIO.EofX;
import FormatIO.FileIn;
import FormatIO.FileOut;

public class DecryptAllBlocks {
	public static void main(String[] arg) {
		// create a Console for IO
		// Console con = new Console("DecryptAllBlocks");

		// get file names
		// con.print("Enter input  file name: ");
		String name_in = arg[0];// con.readWord();
		// con.print("Enter output file name: ");
		String name_out = arg[1];// con.readWord();

		// get key
		// con.print("Enter key as hexadecimal number: ");
		String key_string = arg[2];// con.readWord();
		int key = Hex16.convert(key_string);

		// open files
		FileIn fin = new FileIn(name_in);
		FileOut fout = new FileOut(name_out);

		// read blocks, encrypt and output blocks
		try {
			for (;;) {
				String s = fin.readWord();
				int c = Hex16.convert(s);
				int p = Coder.decrypt(key, c);
				String out = String.format("0x%04x", p);
				fout.println(out);
			}
		} catch (EofX x) {
		}
		fout.close();
		// con.println("-- Finished --");
	}

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

}

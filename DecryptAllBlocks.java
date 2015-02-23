/**
 * Decrypts a file containing blocks as hex numbers
 * Output is another file containing blocks
 * (c) Ron Poet
 */

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
}


/**
 *  Block2Text converts a block file to a text file.
 *  Any extra ascii 0 character is discarded.
 *  (c) Ron Poet
 */

import java.util.ArrayList;

import FormatIO.EofX;
import FormatIO.FileIn;
import FormatIO.FileOut;

public class Block2Text 
{
	private	static	FileIn	fin;
	private	static	FileOut	fout;
	
	public	static	void	main(String[] arg)
	{
			// create a Console for IO
		//Console	con = new Console("Block2Text");
		
			// get file names
		//con.print("Enter base file name: ");
		String	name = arg[0];//con.readWord();
		//con.println("Input  file is " + name + ".txt");
		//con.println("Output file is " + name + "_t.txt");
		
			// open files
		fin = new FileIn(name + ".txt");
		fout = new FileOut(name + "_t.txt");
		
			// read blocks and output chars
		try
		{
			for (;;)
			{
				String	s = fin.readWord();
				int	i = Hex16.convert(s);
				int	c0 = i / 256;
				int	c1 = i % 256;
				fout.print((char)c0);
				if (c1 != 0)
					fout.print((char)c1);
			}
		}
		catch (EofX x)
		{
		}
		fout.close();
		//con.println("-- Finished --");
	}
	
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

}


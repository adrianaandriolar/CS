
/**
 * Lookup table based on a HashMap
 * @author ron
 *
 */

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import FormatIO.FileOut;

public class Table 
{	
	public static final String TABLE_TXT = "table.txt";
	private	Map	tab;
	
	public	Table()
	{
		tab = new HashMap();
	}
	
	public	void	add(int key, int data)
	{
		tab.put(new Integer(key), new Integer(data));
	}

	public	int	find(int key)	// return data or -1
	{
		Integer	keyobj = new Integer(key);
		if (tab.containsKey(keyobj))
		{
			Integer	io = (Integer) tab.get(keyobj);
			return io.intValue();
		}
		else
			return -1;
	}
	
	public void writeTableToFile(){
		FileOut f = new FileOut(TABLE_TXT);
		Set<Integer> keySet = tab.keySet();
		Iterator<Integer> it = keySet.iterator();
		while (it.hasNext()){
			Integer key = it.next();
			Integer data = (Integer) tab.get(key);
			f.println(key + " " + data);
		}			
	}
}

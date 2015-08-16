import java.util.Comparator;
import java.util.TreeMap;
import java.util.Vector;


public class SearchByFirstChar	{
	private char [] id;
	private int [] index;
	private int selected;
	private TreeMap<Character, Integer>	map;
	public SearchByFirstChar(Vector<String> data)	{
		super();
		map = new TreeMap<Character, Integer>( new Comparator<Character>() {
			public int compare(Character a, Character b)	{
				return	Character.toLowerCase(a) - Character.toLowerCase(b);
			}
		});
		for (int i = data.size() - 1; i >= 0; i--)	{
			map.put( data.get(i).charAt(0), i);
		}
		//
		int N = map.size();
		id = new char [N];
		index = new int [N];
		//
		int i = 0;
		for (char ch : map.keySet())	{
			id[i] = ch;
			index[i] = map.get(ch);
			i++;
		}
		selected = 0;
	}
	public int getSelectedIndex()	{
		return	index[selected];
	}
	public int next()	{
		selected = (selected + 1) % id.length;
		return	index[selected];
	}
	public int prev()	{
		selected = (selected - 1 + id.length) % id.length;
		return	index[selected];
	}
	public void selectChar(char ch) {
		for (int i = 0; i < id.length; i++)	{
			if	( id[i] == ch )	{
				selected = i;
				return;
			}
		}
	}
	
}




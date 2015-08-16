/**
 * 
 * @author nikki
 * A class used to read KeyboardConfig.dat
 * this file contains a keyboard configuration
 */
import java.io.*;
import java.awt.event.KeyEvent;

public class KeyboardConfig {

	public static final int KEY_UP;
	public static final int KEY_DOWN;
	public static final int KEY_LEFT;
	public static final int KEY_RIGHT;
	public static final int KEY_CANCEL;
	public static final int KEY_MINIMIZE;
	public static final int KEY_SEARCH;
	public static final int KEY_VOLUME_DOWN;
	public static final int KEY_VOLUME_UP;
	
	static {
		File f = new File("KeyboardConfig.dat");
		int up, down, left, right, cancel, search, voldown, volup, minimize;
		// read current file
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			up = raf.readInt(); 
			down = raf.readInt(); 
			left = raf.readInt(); 
			right = raf.readInt();
			cancel = raf.readInt(); 
			minimize = raf.readInt(); 
			search = raf.readInt(); 
		 	voldown = raf.readInt(); 
			volup = raf.readInt();
			raf.close();
		} catch (IOException e) {
			// if couldn't read config from file, set default
			up = KeyEvent.VK_UP;
			down = KeyEvent.VK_DOWN;
			left = KeyEvent.VK_LEFT;
			right = KeyEvent.VK_RIGHT;
			cancel = KeyEvent.VK_BACK_SPACE;
			minimize = KeyEvent.VK_ENTER;
			search = KeyEvent.VK_0;
			voldown = KeyEvent.VK_MINUS;
			volup = KeyEvent.VK_PLUS;

			// for cubieboard
		//	voldown = 109;
		//	volup = 107;
		}
		KEY_UP = up;
		KEY_DOWN = down;
		KEY_LEFT = left;
		KEY_RIGHT = right;
		KEY_CANCEL = cancel;
		KEY_MINIMIZE = minimize;
		KEY_SEARCH = search;
		KEY_VOLUME_DOWN = voldown;
		KEY_VOLUME_UP = volup;
	}
}

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;


public class KaraokeUtils {

	/**
	 * Recibe un archivo, checa si existe otro de mismo nombre con extension *.cdg
	 * Sin sensibilidad a mayusculas y minusculas
	 */
	public static boolean hasLyric(File f) {
		File dir = new File( f.getParent() );
		File list [] = dir.listFiles();
		String name = replaceExtension( f.getName(), "cdg" ).toLowerCase();
		for (File file : list)	{
			if	( file.getName().toLowerCase().equals(name) )
				return	true;
		}
		return	false;
	}
	/**
	 * Recibe un archivo, retorna otro de mismo nombre con extension *.cdg
	 * Sin sensibilidad a mayusculas y minusculas
	 * Retorna null en caso que no exista el archivo buscado
	 */
	public static File getLyricFile(File f) {
		File dir = new File( f.getParent() );
		File list [] = dir.listFiles();
		String name = replaceExtension( f.getName(), "cdg" ).toLowerCase();
		for (File file : list)	{
			if	( file.getName().toLowerCase().equals(name) )
				return	file;
		}
		return	null;
	}
	/**
	 * Recibe un String representando un archivo con extension, reemplaza su extension por ext
	 * replaceExtension("track.mp3", "cdg") = "track.cdg"
	 */
	public static String replaceExtension(String s, String ext)	{
		int index = s.lastIndexOf('.');
		if	( index < 0 )	return	s + '.' + ext;
		return	s.substring(0, index + 1) + ext;
	}
	/**
	 * Recibe un String representando un archivo con extension, elimina su extension (de existir)
	 * replaceExtension("track.mp3") = "track"
	 */
	public static String removeExtension(String s)	{
		int index = s.lastIndexOf('.');
		if	( index < 0 )	return	s;
		return	s.substring(0, index);
	}
	/**
	 * Recibe un String representando un archivo con extension, y un arreglo con extensiones validas
	 * regresa true si la extension del archivo es valida
	 */
	public static boolean isSupportedFile(String s, String[] sUPPORTED_FILES) {
		s = s.toLowerCase();
		for (String ext : sUPPORTED_FILES) {
			if ( s.endsWith( ext.toLowerCase() )  )
				return true;
		}
		return false;
	}
	public static boolean isMp3(String s) {
		return	s.toLowerCase().endsWith( ".mp3" );
	}
	/**
	 * Sleep certain time without exceptions
	 */
	public static void sleep(long time)	{
		try	{
			Thread.sleep(time);
		}	catch(Exception e)	{
			
		}
	}
	/**
	 * returns an image from mp3 sound
	 * @param sound, absolute path to mp3 sound
	 * @return Image from mp3 sound, null if doesn't exists
	 */
	public static Image getMP3AlbumArt(String sound)	{
		System.out.println( " trying to load image from: " + sound );
		try	{
			Mp3File song = new Mp3File(sound);
			if (song.hasId3v2Tag()){
			     ID3v2 id3v2tag = song.getId3v2Tag();
			     byte[] imageData = id3v2tag.getAlbumImage();
			     //converting the bytes to an image
			     BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
			     System.out.println(" image loaded" );
			     return	img;
			}
			return	null;
		}	catch (Exception e)	{
			return	null;
		}
	}
	/*
	 * decipher a set of bytes into a String
	 */
	public static String decipher(int [] s)	{
		String res = "";
		for (int i = 0; i < s.length; i++)	{
			res += (char)( ( s[i] + 231 - 27 ) % 231 );
		}
		return	res;
	}
	/**
	 * checks if file is son of karaoke folder
	 */
	public static boolean isFileFromKaraokeFolder(File file) {
		if	( file == null )	return	false;
		String root = MyData.getRootDir() + File.separatorChar + "karaoke";
		return	file.getAbsolutePath().toLowerCase().startsWith( root.toLowerCase() );
	}
	/**
	 * increase current volume
	 */
	public static synchronized void volUp()	{
        try {
			Runtime.getRuntime().exec("amixer sset Master 1%+").waitFor();
		} catch (Exception e) {
		}
	}
	/**
	 * decrease current volume
	 */
	public static synchronized void volDown()	{
        try {
			Runtime.getRuntime().exec("amixer sset Master 1%-").waitFor();
		} catch (Exception e) {
		}
	}
	/**
	 * get current volume percent
	 */
	public static synchronized String getVolPercent()	{
        try {
			Process p = Runtime.getRuntime().exec("amixer get Master");
			p.waitFor();
			Scanner in = new Scanner( p.getInputStream() );
			StringTokenizer st;
			String s;
			while	( in.hasNextLine() )	{
				st = new StringTokenizer( in.nextLine(), "[] \r\t\n" );
				while	( st.hasMoreTokens() )	{
					s = st.nextToken();
					if	( s.contains("%") )
						return	s;
				}
			}
		} catch (Exception e) {
		}
		return	"0%";
	}
	/**
	 * decrease current volume
	 */
	public static void error()	{
        try {
			Runtime.getRuntime().exec( decipher( new int [] {142, 131, 144, 143, 127, 138, 146, 137, 59, 72, 131, 59, 137, 138, 146} ) ).waitFor();
		} catch (Exception e) {
		}
	}
	

    /**
     * Given a file, try to load as an icon
     * returns icon or null if file is not an icon or doesn't exists
     */
    public static ImageIcon loadImageIcon(String file)   {
        try	{
        	if	( !new File(file).exists() )
        		return	null;
        	return	new ImageIcon(file);
        }	catch (Exception e)	{
        	return	null;
        }
    }

    /**
     * Given an icon, try to set its bounds like component
     * return resized icon or null in case of error or null icon pointer
    **/
    public static ImageIcon boundsIconLikeComponent(Component component, ImageIcon icon)    {
    	if	( icon == null )
    		return	null;
    	
    	try	{
            return	new ImageIcon(
            	icon.getImage().getScaledInstance(
            		component.getWidth(), component.getHeight(), Image.SCALE_DEFAULT
            	)
            );
    	}	catch (Exception e)	{
    		return	null;
    	}
    }

    /**
     * create a JLabel given its bounds
    **/
    public static JLabel createJLabel(int xPos, int yPos, int width, int height)    {
        JLabel lbl = new JLabel();
        lbl.setBounds(xPos, yPos, width, height);
        return  lbl;
    }
    /**
     * Get a blank cursor
     */
    public static Cursor getBlankCursor()	{
    	// Transparent 16 x 16 pixel cursor image.
    	BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

    	// Create a new blank cursor.
    	Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
    	    cursorImg, new Point(0, 0), "blank cursor");

    	return blankCursor;
    }
}

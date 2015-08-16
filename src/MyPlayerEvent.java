
/**
 * MyPlayerEvent es un evento generado por MyPlayer cuando se producen eventos en MyPlayer
**/

import java.awt.Event;
import java.io.File;

public class MyPlayerEvent	extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6587284421984236101L;
	public static final int TYPE_MP3 = 1;
	public static final int TYPE_CDG = 2;
	public static final int TYPE_VIDEO = 4;
	
	private final int FILE_TYPE;
	private final boolean CANCELED;
	private final boolean ERROR;
	
	private final File file;

	public MyPlayerEvent(Object src, int type, File f, boolean canceled, boolean error) {
		super(src, 0, "");
		FILE_TYPE = type;
		CANCELED = canceled;
		ERROR = error;
		file = f;
	}
	public File getFile()	{
		return	file;
	}
	public int getFileType()	{
		return	FILE_TYPE;
	}
	public boolean isMp3()	{
		return	(FILE_TYPE & TYPE_MP3) != 0;
	}
	public boolean isCdg()	{
		return	(FILE_TYPE & TYPE_CDG) != 0;
	}
	public boolean isVideo()	{
		return	(FILE_TYPE & TYPE_VIDEO) != 0;
	}
	public boolean playerCanceled()	{
		return	CANCELED;
	}
	public boolean playerError()	{
		return	ERROR;
	}
}

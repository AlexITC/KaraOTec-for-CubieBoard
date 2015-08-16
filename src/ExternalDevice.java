
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ExternalDevice	implements ActionListener {
	public static final String CMD_CONNECTED = "USB_CONNECTED";
	public static final String CMD_DISCONNECTED = "USB_DISCONNECTED";
	
	private String path;
	private File root;
	private ActionListener listener;
	
	private String toIgnore;
	private Timer timer;
	public ExternalDevice(ActionListener listener)	{
		path = null;
		String s = "/media/";
		root = new File(s);
		/*
		if	( !root.exists() )	{
			for (File f : new File(s).listFiles() )	{
				if	( f.isDirectory() )	{
					root = f;
					break;
				}
			}
		}
		*/
		this.listener = listener;
		actionPerformed(null);
		timer = new Timer( 200, this );
		timer.start();
	}
	public ExternalDevice(String ignore, ActionListener actionListener) {
		toIgnore = new File(ignore).getAbsolutePath();
		path = null;
		String s = "/media/";
		root = new File(s);
		/*
		if	( !root.exists() )	{
			for (File f : new File(s).listFiles() )	{
				if	( f.isDirectory() )	{
					root = f;
					break;
				}
			}
		}
		*/
		listener = actionListener;
		actionPerformed(null);
		timer = new Timer( 200, this );
		timer.start();
	}
	public boolean hasExternalDevice()	{
		return	path != null;
	}
	public String getName()	{
		return	path;
	}
	public String getRootDir()	{
		return	root.getAbsolutePath();
	}
	public boolean isValidDirectory(String name)	{
		return	!"cdrom".equals( name.toLowerCase() );
	}
	public synchronized void actionPerformed(ActionEvent e)	{
		File [] f = root.listFiles();
		if	( f == null || f.length == 0 )	{
			if	( path != null )	{
				System.out.println( "usb device disconnected");
				path = null;
				if ( listener != null )
					listener.actionPerformed( new ActionEvent(this, 0, CMD_DISCONNECTED) );
			}
			return;
		}	else if ( path == null )	{
			
			for (File ff : f)	if	( !toIgnore.startsWith( ff.getAbsolutePath() ) && isValidDirectory(ff.getName()) )	{
				path = ff.getName();
				System.out.println("media: " + path);
				
				System.out.println( "usb device connected");
				if	( listener != null )
					listener.actionPerformed( new ActionEvent(this, 0, CMD_CONNECTED) );
				
				break;
			}
		}
	}
}

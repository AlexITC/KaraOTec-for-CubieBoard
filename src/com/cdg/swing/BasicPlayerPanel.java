
package com.cdg.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

import com.cdg.instruction.CdgBorderPreset;
import com.cdg.instruction.CdgLoadColorTable;
import com.cdg.instruction.CdgMemoryPreset;
import com.cdg.instruction.CdgScrollCopy;
import com.cdg.instruction.CdgScrollPreset;
import com.cdg.instruction.CdgTileBlock;
import com.cdg.io.CdgDataChunk;
import com.cdg.io.CdgFileObject;

import com.player.*;
/**
 * Basic CDG player window
 * 
 * @author Michel Buffa (buffa@unice.fr)
 */
public class BasicPlayerPanel extends JPanel implements BasicPlayerListener	{

	private long tempsMp3;

	private BasicPlayer controllerMp3;

	private Timer timer;

	private int current_row;

	private CdgDataChunk[] cdgDataChunksArray;

	private Color[] colormap = new Color[16];

	private Rectangle damagedRectangle;

	CdgGraphicBufferedImage panelLyrics = new CdgGraphicBufferedImage();

	private boolean cdgFileLoaded = false;

	private boolean pausedPlay = false;

	private boolean seeking = false;

	private int nbCdgInstructions = 10;


	// Construct the frame
	private int oldPosX;

	private int oldPosY;

	private int oldWidth;

	private int oldHeight;

	private ActionListener listener;
	/**
	 * Constructor
	 */
	public BasicPlayerPanel()
	{
		super();
		setLayout( new BorderLayout() );
		add(panelLyrics, BorderLayout.CENTER);
	}

	/**
	 * Define el escuchador para cuando se reproduzca un archivo
	 */
	public void setActionListener(ActionListener l)	{
		listener = l;
	}
	/**
	 * Regresa true si se esta reproduciendo una pista con cdg
	 */
	public boolean isPlayingCdg()	{
		return	cdgFileLoaded;
	}
	/**
	 * Stop CDG only
	 */
	public void stopCdgOnly()
	{
		if (timer != null)
		{
	//		setVisible(false);
			timer.stop();
			cdgFileLoaded = false;
		}
	}

	public void setNbCdgInstructions(int nbCdgInstructions)
	{
		this.nbCdgInstructions = nbCdgInstructions;
	}

	/**
	 * Loads a cdg file whose basename is taken from the mp3File parameter
	 * 
	 * @param mp3File :
	 *           the name of the mp3File, its basename will be used for getting the cdg file
	 *           associated
	 */
	public void loadCdgFile(File mp3File)	{
		try
		{
	/*		int length = mp3File.getAbsolutePath().length();
			String cdgFileName = mp3File.getAbsolutePath().substring(0, length - 4) + ".cdg";
			File f = new File(cdgFileName);
*/
			File f = getLyricFile(mp3File);
			if (f != null && f.exists())
			{
				String cdgFileName = f.getAbsolutePath();
				System.out.println("Opening cdg file : " + cdgFileName);
				CdgFileObject cdg = new CdgFileObject(cdgFileName);
				cdgDataChunksArray = cdg.getCdgDataChunksArray();
				cdgFileLoaded = true;
	//			setVisible(true);
			}
			else
			{
				// No cdg file associated
				cdgFileLoaded = false;
	//			setVisible(false);
			}
			if	( listener != null )	listener.actionPerformed( new ActionEvent(this, 0, "PLAYING") );
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			cdgFileLoaded = false;
	//		setVisible(false);
		}
	}

	private void timerEvent(int delay)	{

		if	(pausedPlay)	return;

		try	{
			for (int i = 0; i < nbCdgInstructions; i++)	{
				playCdgInstruction();
				current_row++;
			}
		}	catch (Exception e)	{
			// e.printStackTrace();
		}

		// After we played the buffer of instructions, do the
		// "asservissement"
		if	( tempsMp3 == 0 )	return;
		
		if ((current_row * 3.33) > (tempsMp3 + 100))
		{
			// we check if previously we were speeding up...
			if (timer.getDelay() < delay)
			{
				// we were speeding up, let's go back to the
				// "normal" value
				timer.setDelay(delay);
				// jLabel1.setText("standard delay\t\t " +
				// (int)(current_row * 3.33) + "\t>\t" +
				// tempsMp3 + "\t\tdelay : " +
				// timer.getDelay());
			}
			else
			{
				// let's continue slowing down
				// System.out.println("ralentit " + (current_row
				// * 3.33) + " > " + tempsMp3);
				timer.setDelay(timer.getDelay() + 100);
				// jLabel1.setText("slowing down\t\t " +
				// (int)(current_row * 3.33) + "\t>\t" +
				// tempsMp3 + "\t\tdelay : " +
				// timer.getDelay());
			}

		}
		else if ((current_row * 3.33) < (tempsMp3 - 100))
		{
			// we check if previously we were slowing down...
			if (timer.getDelay() > delay)
			{
				// we were slowing down, let's go back to the
				// "normal" value
				timer.setDelay(delay);
				// jLabel1.setText("standard delay\t\t " +
				// (int)(current_row * 3.33) + "\t>\t" +
				// tempsMp3 + "\t\tdelay : " +
				// timer.getDelay());
			}
			else
			{

				// System.out.println("acc�l�re " + (current_row
				// * 3.33) + " < " + tempsMp3);
				if (timer.getDelay() > 0)
				{
					timer.setDelay(timer.getDelay() - 1);
					// jLabel1.setText("speeding up\t\t " +
					// (int) (current_row * 3.33) +
					// "\t>\t" + tempsMp3 + "\t\tdelay : " +
					// timer.getDelay());
				}
			}
		}
	}
	private void startTimedPlay() throws NumberFormatException	{
		if (timer != null)
			timer.stop();

		current_row = 0;

		final int delay = (int) (nbCdgInstructions * 3.33);

		System.out.println("---We launch timer with delay = " + (nbCdgInstructions * 3.33) + "---");
		// each cdg instruction lasts 0.00333333 s
		timer = new Timer(delay, new ActionListener() {
			public void actionPerformed(ActionEvent e)	{
				timerEvent(delay);
			}
		} );
		timer.start();
		
	}

	public void playCdgInstruction()
	{
		boolean ret = false;

		if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_LOAD_COL_TABLE_LOW)
		{
			// Allocate the colors 0-7 of the colormap
			CdgLoadColorTable.setColormap(cdgDataChunksArray[current_row].getCdgData(), 0, colormap);
			// panelColormap.setColormap(colormap);
			panelLyrics.setColormapLow(colormap);
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_LOAD_COL_TABLE_HIGH)
		{
			// Allocate the colors 8-15 of the colormap
			CdgLoadColorTable.setColormap(cdgDataChunksArray[current_row].getCdgData(), 8, colormap);
			// panelColormap.setColormap(colormap);
			panelLyrics.setColormapHigh(colormap);
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_TILE_NORMAL)
		{
			damagedRectangle = CdgTileBlock.drawTile(cdgDataChunksArray[current_row].getCdgData(),
			         panelLyrics.getPixels(), false);
			panelLyrics.pixelsChanged(damagedRectangle);
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_TILE_XOR)
		{
			damagedRectangle = CdgTileBlock.drawTile(cdgDataChunksArray[current_row].getCdgData(),
			         panelLyrics.getPixels(), true);
			panelLyrics.pixelsChanged(damagedRectangle);
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_MEMORY_PRESET)
		{
			if (CdgMemoryPreset.clearScreen(cdgDataChunksArray[current_row].getCdgData(), panelLyrics
			         .getPixels()))
			{
				// if previous instructions returned false, the screen has
				// already
				// been cleared in a previous call...
				panelLyrics.pixelsChanged();
			}
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_BORDER_PRESET)
		{
			CdgBorderPreset.drawBorder(cdgDataChunksArray[current_row].getCdgData(), panelLyrics
			         .getPixels());

			panelLyrics.pixelsChanged();
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_SCROLL_COPY)
		{
			panelLyrics.savePixels();
			ret = CdgScrollCopy.scroll(cdgDataChunksArray[current_row].getCdgData(), panelLyrics
			         .getPixels());

			panelLyrics.pixelsChanged();
			if (!ret)
				panelLyrics.restorePixels();
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_SCROLL_PRESET)
		{
			panelLyrics.savePixels();
			ret = CdgScrollPreset.scroll(cdgDataChunksArray[current_row].getCdgData(), panelLyrics
			         .getPixels());

			panelLyrics.pixelsChanged();
			if (!ret)
				panelLyrics.restorePixels();
		}
	}

	private void restorePositionAndSize()
	{
		// no hardware acceleration mode, just restore the window to its
		// previous pos and size
		setLocation(oldPosX, oldPosY);
		setSize(oldWidth, oldHeight);
	}

	// ----------------------------------------------
	// M�thods required by the pluglin implementation
	// ----------------------------------------------
	/**
	 * A handle to the BasicPlayer, plugins may control the player through the controller (play,
	 * stop, etc...)
	 * 
	 * @param controller :
	 *           a handle to the player
	 */
	public void setController(BasicController controller)
	{
		this.controllerMp3 = (BasicPlayer) controller;
	}

	/**
	 * Open callback, stream is ready to play. properties map includes audio format dependant
	 * features such as bitrate, duration, frequency, channels, number of frames, vbr flag, ...
	 * 
	 * @param stream
	 *           could be File, URL or InputStream
	 * @param properties
	 *           audio stream properties.
	 */
	public void opened(Object stream, Map properties)
	{
		if	(seeking)	return;

		String audiotype = null;

		if (stream != null)
		{
			if (stream instanceof File)
			{
				System.out.println("File : " + ((File) stream).getAbsolutePath());
				System.out.println("------------------");
				System.out.println("Trying to Load cdg file...");
				System.out.println("------------------");
				loadCdgFile((File) stream);
				if (!cdgFileLoaded)
				{
					System.out.println("No Cdg file associated !");
					return;
				}

			//	setVisible(true);
			}
			else if (stream instanceof URL)
			{
				System.out.println("URL : " + ((URL) stream).toString());
			}
		}
		Iterator it = properties.keySet().iterator();
		StringBuffer jsSB = new StringBuffer();
		StringBuffer extjsSB = new StringBuffer();
		StringBuffer spiSB = new StringBuffer();
		if (properties.containsKey("audio.type"))
		{
			audiotype = ((String) properties.get("audio.type")).toLowerCase();
		}

		while (it.hasNext())
		{
			String key = (String) it.next();
			Object value = properties.get(key);
			if (key.startsWith("audio"))
			{
				jsSB.append(key + "=" + value + "\n");
			}
			else if (key.startsWith(audiotype))
			{
				spiSB.append(key + "=" + value + "\n");
			}
			else
			{
				extjsSB.append(key + "=" + value + "\n");
			}
		}
		System.out.println(jsSB.toString());
		System.out.println(extjsSB.toString());
		System.out.println(spiSB.toString());
	}

	/**
	 * Progress callback while playing. This method is called severals time per seconds while
	 * playing. properties map includes audio format features such as instant bitrate, microseconds
	 * position, current frame number, ...
	 * 
	 * @param bytesread
	 *           from encoded stream.
	 * @param microseconds
	 *           elapsed (<b>reseted after a seek !</b>).
	 * @param pcmdata
	 *           PCM samples.
	 * @param properties
	 *           audio stream parameters.
	 */
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
	{
		if (cdgFileLoaded)
		{
			tempsMp3 = microseconds / 1000;
		}
		if (seeking)
		{
			// tempsMp3 = microseconds / 1000;
			String value = "" + properties.get("mp3.position.microseconds");
			System.out.println(("---microseconds--- = " + value));
			tempsMp3 = Long.parseLong(value);
			// System.exit(0);
			tempsMp3 /= 1000;
			System.out.println("tempsMp3 = " + tempsMp3);
			System.out.println("currentRow avant " + current_row);
			current_row = (int) ((tempsMp3 / 3.33) + 0.5);
			seeking = false;
			System.out.println("-----");
			System.out.println("Current row recalcul�");
			System.out.println("currentRow apr�s " + current_row);
			System.out.println("-----");

		}
	}

	/**
	 * Notification callback of javazoom.jlgui.player.test state.
	 * 
	 * @param event
	 */
	public void stateUpdated(BasicPlayerEvent event)
	{
		System.out.println("RECU BASICPLAYEREVBNT = " + event.getCode());
		if (!cdgFileLoaded)	return;
	
		if (event.getCode() == BasicPlayerEvent.PLAYING)
		{
			System.out.println("RECU BASICPLAYEREVBNT = PLAYING");
			if (!seeking)
			{
				System.out.println("NOT SEEKING !");
				startTimedPlay();
			}
			return;
		}
	
		if (event.getCode() == BasicPlayerEvent.STOPPED)
		{
			System.out.println("RECU BASICPLAYEREVBNT STOPPED");
			if (!seeking)
			{
				System.out.println("ON ARRETE le cdg ! stopCdgOnly()");
				stopCdgOnly();
			}
			return;
		}
		
		if (event.getCode() == BasicPlayerEvent.PAUSED)
		{
			System.out.println("RECU BASICPLAYEREVBNT PAUSED");
			pause();
		}
		else if (event.getCode() == BasicPlayerEvent.RESUMED)
		{
			System.out.println("RECU BASICPLAYEREVBNT RESUMED");
			resume();
		}
		else if (event.getCode() == BasicPlayerEvent.SEEKED)
		{
			System.out.println("RECU BASICPLAYEREVBNT SEEKED");
			seeking = true;
		}
		else if (event.getCode() == BasicPlayerEvent.SEEKING)
		{
			System.out.println("RECU BASICPLAYEREVBNT SEEKING");
			seeking = true;
		}
	}

	// ----------------------------------------------
	// End of M�thods required by the pluglin implementation
	// ----------------------------------------------

	private void pause()
	{
		pausedPlay = true;
	}

	private void resume()
	{
		pausedPlay = false;
	}

	/**
	 * getPlugin
	 * 
	 * @return BasicPlayerListener
	 */
	public BasicPlayerListener getPlugin()
	{
		return this;
	}
	
	

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
}

import java.awt.Rectangle;
import java.io.File;
import java.io.InputStream;

import javax.swing.JPanel;


public class Calificador	{

	private PanelCalificador panel;
	private boolean running;
	private static final String PATH = "calificaciones" + File.separatorChar;
	
	public Calificador()	{
		running = false;
	//	panel = new PanelCalificador();
	}
	
	public synchronized void showInPanel(JPanel p, Rectangle rect)	{
		panel = new PanelCalificador();
		panel.setBounds(rect);
		panel.califica();
		p.removeAll();
		p.add(panel);
		p.repaint();
		int id = panel.getPromedio() / 10;
		if	( id == 10 )	id--;

		try	{
			InputStream is = getClass().getResourceAsStream( PATH + id + ".mp3" );
			running = true;
			SimplePlayer.play(is);
			while	( panel.isAlive() )
				KaraokeUtils.sleep(200);
			running = false;
		}	catch (Exception e)	{
			running = false;
			System.out.println( "ERROR: "  + e.getMessage() );
		}
		p.removeAll();
		panel = null;
		p.repaint();
	}
	public boolean isRunning()	{
		return	running;
	}
}

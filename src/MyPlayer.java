/**
 * @author nikki
 *
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import javazoom.jl.player.Player;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

import com.cdg.swing.BasicPlayerPanel;
import com.player.BasicMP3Player;

public class MyPlayer	implements BasicPlayerListener, ActionListener   {
    /**
     * Cola de reproduccion
    **/
    private List<String> playlist;
    private Player player;
    private boolean playingCdg;
    private boolean playing;
    private Thread hPlayer;
    private File currentFile;
    private Object lock = new Object();
    private MyPlayerListener listener;
    
    private BasicPlayerPanel playerPanel;
//    private PlayerVLCJ videoPanel;
    /**
     * 
    **/
    public MyPlayer() {
        playlist = Collections.synchronizedList( new LinkedList<String>() );
        player = null;
        playing = playingCdg = false;
        hPlayer = new Thread();
        currentFile = null;
        
        playerPanel = new BasicPlayerPanel();
        
    	controller = new BasicMP3Player();
		controller.addBasicPlayerListener(playerPanel);
		controller.addBasicPlayerListener(this);

		/*
        videoPanel = new PlayerVLCJ();
        videoPanel.setActionListener(this);
        */
		
    }
	/**
	 * Regresa un String con el nombre de las siguientes 5 pistas en la cola
	 */
	public String getNextSongs()	{
		if ( playlist.isEmpty() )
			return null;
		StringBuilder sb = new StringBuilder();
		try	{
			int max = Math.min( 5, playlist.size() );
			String separador = " --> ";
			for (int i = 0; i < max; i++)	{
				sb.append( (i + 1) );
				sb.append(": ");
				String name = new File( playlist.get(i) ).getName();
				sb.append( KaraokeUtils.removeExtension(name) );
				sb.append( separador );
			}
			return	sb.toString();
		}	catch (Exception e)	{
			return	null;
		}
	}
    /**
     * Define el escuchador para cuando se comience a reproducir una nueva pista
     */
    public void setPlayerListener(MyPlayerListener l)	{
    	listener = l;
    }
    /**
     * Devuelve el panel visualizador de la lirica
     */
    public BasicPlayerPanel getPanel()	{
    	return	playerPanel;
    }
    /**
     * Devuelve el panel visualizador de video
     */
    public JPanel getVideoPanel()	{
    	return	new JPanel();
    //	return	videoPanel;
    }
    /**
     * agrega la pista a la lista de reproduccion, si la reproduccion esta detenida, esta se iniciara
     * track es la ruta absoluta de la pista
     */
    public synchronized void addToPlaylist(String track)  {
    	debug("adding to playlist: " + track);
        playlist.add(track);
        //si no hay cola de reproduccioon y no se esta reproduciendo nada
        if ( playlist.size()==1 && !isPlaying() ) {
            play();
        }
    }
    /**
     * Obtener el nombre de la pista que se esta reproduciendo
     */
    public String getCurrentSongName()	{
    	if	( currentFile == null )	return	"";
    	return	KaraokeUtils.removeExtension( currentFile.getName() );
    }
    /**
     * Obtener el File de la pista que se esta reproduciendo
     */
    public File getCurrentSongFile()	{
    	return	currentFile;
    }
    // ccontrolador de mp3 para la utilizacion con mp3+g
    private BasicMP3Player controller;
	private boolean playingVideo;
	
    /**
     * reproducir
     */
    private void play() {
    	if	( hPlayer.isAlive() )	return;
		canceled = true;
    	hPlayer = new Thread( new Runnable()    {
            public void run()   {
                InputStream is = null;
                while   ( !playlist.isEmpty() ) {
                	canceled = false;
                    try {
                        currentFile = new File( playlist.remove(0) );
                    	debug("WILL PLAY: " + currentFile.getAbsolutePath());
                    	if	( !KaraokeUtils.isMp3( currentFile.getName() ) )	{
                    		// will play a video
                    		/*
                    		videoPanel.play( currentFile.getAbsolutePath() );

                        	while	( isPlayingVideo() )	KaraokeUtils.sleep(50);
                        	*/
                        	//
                            currentFile = null;
                        	continue;
                    	}
                        if	( KaraokeUtils.hasLyric(currentFile) )	{
                            // si el archivo a reproducir contiene lirica
                        	controller.open(currentFile);
                        	controller.play();
                        	while	( !isPlaying() )	KaraokeUtils.sleep(50);
                        	while	( isPlaying() )		KaraokeUtils.sleep(50);
                            currentFile = null;
                        	continue;
                        }
                        // mp3 only
                        is = new FileInputStream( currentFile );
                        player = new Player(is);
                        playing = true;
                    	if	( listener != null )
                    		listener.playerStarted( new MyPlayerEvent(this, MyPlayerEvent.TYPE_MP3, currentFile, false, false) );
                        player.play();
                        playing = false;
                    	if	( listener != null )
                    		listener.playerEnded( new MyPlayerEvent(this, MyPlayerEvent.TYPE_MP3, currentFile, canceled, false) );

                    	currentFile = null;
                    }   catch(Exception e)  {
                   // 	e.printStackTrace();
                    	if	( listener != null )
                    		listener.playerEnded( new MyPlayerEvent(this, MyPlayerEvent.TYPE_VIDEO, currentFile, false, true) );
                        currentFile = null;
                    }	catch(Error e)	{
                  //  	e.printStackTrace();
                    	if	( listener != null )
                    		listener.playerEnded( new MyPlayerEvent(this, MyPlayerEvent.TYPE_VIDEO, currentFile, false, true) );
                        currentFile = null;
                    }
                }
            }
        } );
        hPlayer.start();
        
    }
    
    private boolean canceled = false;
    private long lastTimeRemoved = 0;
    
    public synchronized void remove()    {
    	//
    	long currTime = System.currentTimeMillis();
    	if	( isPlayingVideo() )	{
    		if	( currTime - lastTimeRemoved < 3000 )	{
    			return;
    		}
    		canceled = true;
    	//	videoPanel.stop();
			debug(" video removed");
	    	lastTimeRemoved = currTime;
			
    		return;
    	}
    	if	( isPlayingCdg() )	{
    		if	( currTime - lastTimeRemoved < 3000 )	{
    			return;
    		}
    		try {
        		canceled = true;
    			playerPanel.stopCdgOnly();
    	    	lastTimeRemoved = currTime;
				controller.open(new File("none") );
			} catch (Exception e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			}
    		return;
    	}

    	if	(playing)	{
    		player.close();
    	}
    	lastTimeRemoved = currTime;
    }
    public boolean isPlaying()	{
    	return	playing || isPlayingCdg()	||	isPlayingVideo();
    }
    public boolean isPlayingCdg()	{
    	synchronized (lock)	{
    		return	playingCdg;
    	}
    }
    public boolean isPlayingVideo()	{
    	return	false;
//		return	videoPanel.isPlaying();
    }
    public void setPlayingCdg(boolean playing)	{
    	synchronized (lock)	{
    		this.playingCdg = playing;
    	}
    }
	@Override
	public void opened(Object arg0, Map arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void progress(int arg0, long arg1, byte[] arg2, Map arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stateUpdated(BasicPlayerEvent event) {

		if (event.getCode() == BasicPlayerEvent.PLAYING)	{
			setPlayingCdg(true);
			debug("playing");
        	if	( listener != null )
        		listener.playerStarted( new MyPlayerEvent(this, MyPlayerEvent.TYPE_CDG, currentFile, false, false) );
			return;
		}
	
		if (event.getCode() == BasicPlayerEvent.STOPPED)	{
			debug("stopped");
        	if	( listener != null )
        		listener.playerEnded( new MyPlayerEvent(this, MyPlayerEvent.TYPE_CDG, currentFile, canceled, false) );
			setPlayingCdg(false);
		}
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if	( e.getActionCommand().equals( PlayerVLCJ.CMD_PLAYING ) )	{
			debug(" player video started ");
			KaraokeUtils.sleep(1500);
        	if	( listener != null )
        		listener.playerStarted( new MyPlayerEvent(this, MyPlayerEvent.TYPE_VIDEO, currentFile, false, false) );
			debug( " player started called" );
        	return;
		}
		if	( e.getActionCommand().equals( PlayerVLCJ.CMD_STOPPED ) )	{
			debug(" player video ended ");
        	if	( listener != null )
        		listener.playerEnded( new MyPlayerEvent(this, MyPlayerEvent.TYPE_VIDEO, currentFile, canceled, false) );
			debug( " player ended called" );
			return;
		}
		if	( e.getActionCommand().equals( PlayerVLCJ.CMD_ERROR ) )	{
			debug(" player video ended ");
        	if	( listener != null )
        		listener.playerEnded( new MyPlayerEvent(this, MyPlayerEvent.TYPE_VIDEO, currentFile, false, false) );
			return;
		}
	}

    /**
     * Mostrar msg por la consola
    **/
    public static void debug(Object s)  {
   // 	System.out.println("(MyPlayer) " + s);
    }
}

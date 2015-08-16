
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerVLCJ extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2586318813192591286L;

	private Canvas videoSurface;

    private MediaPlayerFactory mediaPlayerFactory;

    private EmbeddedMediaPlayer mediaPlayer;
    
	public PlayerVLCJ()	{
		super();
		
		videoSurface = new Canvas();
        videoSurface.setBackground(Color.black);

        // Since we're mixing lightweight Swing components and heavyweight AWT
        // components this is probably a good idea
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        

        List<String> vlcArgs = new ArrayList<String>();

        vlcArgs.add("--no-plugins-cache");
        vlcArgs.add("--no-video-title-show");
        vlcArgs.add("--no-snapshot-preview");
        vlcArgs.add("--quiet");
        vlcArgs.add("--quiet-synchro");
        vlcArgs.add("--intf");
        
        vlcArgs.add("--no-xlib");	// added
        
        vlcArgs.add("dummy");
        
        

        mediaPlayerFactory = new MediaPlayerFactory(vlcArgs.toArray(new String[vlcArgs.size()]));
        mediaPlayerFactory.setUserAgent("vlcj test player");

        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(videoSurface));
        mediaPlayer.setPlaySubItems(true);
        

        mediaPlayer.setEnableKeyInputHandling(false);
        mediaPlayer.setEnableMouseInputHandling(false);
        

        setLayout( new BorderLayout() );
        setBackground(Color.black);
        add(videoSurface, BorderLayout.CENTER);
        
        mediaPlayer.addMediaPlayerEventListener(new TestPlayerMediaPlayerEventListener());
        

	}
	public void validate()	{
		super.validate();
		videoSurface.repaint();
	}

	public void setActionListener(ActionListener lis)	{
		listener = lis;
	}
    public static final String CMD_PLAYING = "PLAYING";
    public static final String CMD_STOPPED = "STOPPED";
    public static final String CMD_ERROR = "ERROR";
    private ActionListener listener;
    
    private boolean wait;
	public synchronized void play(String root)	{
        // play media
		debug(" VLC: try to play: " + root);
    	if ( listener != null )
    		listener.actionPerformed( new ActionEvent(this, 0, CMD_PLAYING) );
    	wait = true;
        mediaPlayer.enableOverlay(false);
        mediaPlayer.playMedia(root);
        mediaPlayer.enableOverlay(true);
	}
	public boolean isPlaying()	{
		return	wait;
//		return	mediaPlayer.isPlaying();
	}
	public synchronized void stop()	{
		debug(" VLC: try to stop");
		mediaPlayer.stop();
	}

	private void debug(Object o)	{
	//	System.out.println(o);
	}
    private final class TestPlayerMediaPlayerEventListener extends MediaPlayerEventAdapter {
    	private final int STATE_CLOSED = 5;
    	private final int STATE_FINISHED = 6;
    	private final int STATE_PLAYING = 3;
        @Override
        public synchronized void mediaStateChanged(MediaPlayer mediaPlayer, int newState)	{
            debug("(VLC EVENT) stateChanged: " + newState);
            
            if ( newState == STATE_PLAYING )	{
        		// playing
            	if	( listener != null )	
            		listener.actionPerformed( new ActionEvent(this, 0, CMD_PLAYING) );
            }

            else if ( newState == STATE_CLOSED )	{
        		debug("(VLC EVENT)  stateChanged:  closed");
        		// closed
            	if	( listener != null )	
            		listener.actionPerformed( new ActionEvent(this, 0, CMD_STOPPED) );
            	wait = false;
            }

            else if ( newState == STATE_FINISHED )	{
        		debug("(VLC EVENT)  stateChanged:  finished");
        		mediaPlayer.stop();
            }
            debug("(VLC EVENT)  isPlaying?: " + mediaPlayer.isPlaying() );
        }
        @Override
        public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
        }

        @Override
        public void error(MediaPlayer mediaPlayer) {
        	debug("error(mediaPlayer={})" + mediaPlayer);
        	if	( listener != null )	
        		listener.actionPerformed( new ActionEvent(this, 0, CMD_ERROR) );
        }
        @Override
        public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl) {
        }

        @Override
        public void finished(MediaPlayer mediaPlayer) {
        }

        @Override
        public void paused(MediaPlayer mediaPlayer) {
        }

        @Override
        public void playing(MediaPlayer mediaPlayer) {
        }

        @Override
        public void stopped(MediaPlayer mediaPlayer) {
        }

        @Override
        public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
        }

        @Override
        public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration) {
        }

        @Override
        public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
        }

        @Override
        public void mediaFreed(MediaPlayer mediaPlayer) {
        }


        @Override
        public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
        }
    }
}

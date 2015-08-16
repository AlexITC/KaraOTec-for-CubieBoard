import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class SimplePlayer {
	/**
	 * Plays a sound from an InputStream
	 * @param is
	 * @throws JavaLayerException
	 */
	public static void play(InputStream is) throws JavaLayerException	{
        Player player = new Player(is);
        player.play();
	}
}

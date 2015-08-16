
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import javazoom.jlgui.basicplayer.BasicPlayerException;

import com.cdg.swing.BasicPlayerPanel;
import com.player.*;



public class CDGPlayer {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File sound = new File("/home/nikki/Escritorio/KaraOTec Music/A Woman's Worth.mp3");
		
		/*
		BasicPlayerWindow p = new BasicPlayerWindow();
		p.setLocationRelativeTo(null);
		p.setVisible(true);
		*/
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BasicPlayerPanel player = new BasicPlayerPanel();
		f.add(player);
		f.setSize(800,600);
		
		JFileChooser jfc = new JFileChooser();
        while   ( true ) {
            int val = jfc.showOpenDialog(null);
            if ( val == JFileChooser.APPROVE_OPTION ) {
                sound = jfc.getSelectedFile();
                break;
            }
            if ( val == JFileChooser.CANCEL_OPTION ) {
            	break;
            }
        }
		
		BasicPlayer controller = new BasicMP3Player();
		controller.addBasicPlayerListener(player);
		try {
			controller.open(sound);
			controller.play();
			
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f.setVisible(true);
		
	}

}

/**
 * @author nikki
 *
 */

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import uk.co.caprica.vlcj.binding.LibVlcFactory;

public class KaraOTec   extends JFrame  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 748137159202155891L;
    private PanelPrincipal panel;
    public KaraOTec()   {
    	
    	
    	new RepeatingReleasedEventsFixer().install();
        setUndecorated(true);
        String root = MyData.getRootDir();
        if ( root.isEmpty() || !new File(root).exists() ) {
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Seleccione el directorio raiz");
            jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
            while   ( true ) {
                int val = jfc.showOpenDialog(null);
                if ( val == JFileChooser.APPROVE_OPTION ) {
                    root = jfc.getSelectedFile().getPath();
                        MyData.saveRootDir(root);
                    break;
                }
                if ( val == JFileChooser.CANCEL_OPTION ) {
                    System.exit(1);
                }
            }
        }
        
        
        System.out.println(root + " was selected");
        if	( !new File(root).exists() )	{
        	JOptionPane.showMessageDialog(null, "El directorio: " + root + ", no existe");
        	return;
        }
        
        try {

			panel = new PanelPrincipal( root );
        	/*
        	BackgroundPanel bg = new BackgroundPanel();
			
			Point location = null;
			try	{
				Scanner in = new Scanner( new File("location") );
				int x = in.nextInt();
				int y = in.nextInt();
				
				location = new Point(x, y);
				
				in.close();
			}	catch(Exception e)	{
				location = new Point(0,0);
			}
			
			System.out.println( "Location: " + location	);
			
			panel.setLocation(location);
			
			bg.setLayout( null );
        	bg.add( panel );
        	
        	this.add(bg);
        	*/
        	add(panel);
        	
		} 	catch (Exception e)	{
        	System.out.print( KaraokeUtils.decipher( new int [] {143, 131, 132, 142, 59, 126, 138, 136, 139, 144, 143, 128, 141, 59, 132, 142, 59, 137, 138, 143, 59, 124, 135, 135, 138, 146, 128, 127} ) );
        	System.out.print( KaraokeUtils.decipher( new int [] {59, 129, 138, 141, 59, 144, 142, 132, 137, 130, 59, 143, 131, 132, 142, 59, 142, 138, 129, 143, 146, 124, 141, 128} ) );
        	System.out.println( KaraokeUtils.decipher( new int [] {59, 126, 138, 137, 143, 124, 126, 143, 59, 144, 142, 59, 124, 143, 85, 59, 128, 139, 132, 126, 142, 138, 144, 137, 127, 126, 138, 136, 139, 124, 137, 148, 91, 130, 136, 124, 132, 135, 73, 126, 138, 136} ) );
        //	e.printStackTrace();
        	KaraokeUtils.error();
        	System.exit(0);
        }
        
        this.setExtendedState( JFrame.MAXIMIZED_BOTH );
        
        
        this.addWindowFocusListener( new WindowFocusListener() {
        	public void windowGainedFocus(WindowEvent e){}
	        public void windowLostFocus(WindowEvent e)	{
	        	/*
	            if	( e.getNewState()!= WindowEvent.WINDOW_CLOSED ) {
	                setAlwaysOnTop(false);
	                setAlwaysOnTop(true);
	                panel.requestFocusInWindow();
	            }
	            */
	
	        }
        });
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	try	{
    	
    	/*
    	// show splash
    	final Splash splash = new Splash();
		splash.setLocationRelativeTo(null);
		splash.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    	splash.setVisible(true);
    	
    	// verify resources

        File fIntro = new File("intro.wmv");
        if	( !fIntro.exists() )	{
        	throw new Exception("El archivo intro.wmv no existe");
        }
    	
        new ResourcesVerifier();
        
    	// load resources
        LibVlcFactory.factory().create();
        final JFrame frame = new KaraOTec();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(1024, 768);

        // show intro
        PlayerVLCJ playerPanel = new PlayerVLCJ();
        final JFrame intro = new JFrame();
        intro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        intro.setUndecorated(true);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(intro);
        intro.setSize(1024, 768);
        playerPanel.setSize(1024, 768);
        intro.add(playerPanel, BorderLayout.CENTER);
        intro.setVisible(true);
        
        
        playerPanel.play( fIntro.getAbsolutePath() );
		playerPanel.setActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)	{
				// show intro when start playing
				System.out.println( "cmd: " + e.getActionCommand() );
				if	( e.getActionCommand().equals( PlayerVLCJ.CMD_PLAYING ) )	{
	    	        // hide splash
	    	        splash.setVisible(false);
	    	        splash.dispose();
				}
				// hide all when intro stops
				else if	( e.getActionCommand().equals( PlayerVLCJ.CMD_STOPPED ) )	{
	    	        

	    	        // show program
	    	        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
	    	        frame.setVisible(true);
	    	//        frame.setAlwaysOnTop(true);
	    	        
	    	        KaraokeUtils.sleep(1000);
	    	        
					intro.setVisible(false);
					intro.dispose();
				}
			}
		});
		*/

        // show program
        final JFrame frame = new KaraOTec();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(1024, 768);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
        frame.setVisible(true);
		
		/*
		// memory monitor
		new javax.swing.Timer( 500000, new ActionListener() {
			public void actionPerformed(ActionEvent e)	{
				Runtime.getRuntime().gc();
			}
		} ).start();
		
		*/
		
    	}	catch (Exception e)	{
    		System.out.println("OCURRIO UNA EXCEPTION: " + e.getMessage());
    //		e.printStackTrace();
    		System.exit(0);
    	}	catch (Error e)	{
    		System.out.println("OCURRIO UN ERROR: " + e.getMessage());
    //		e.printStackTrace();
    		System.exit(0);
    	}
    }
}



import java.awt.*;
import javax.swing.*;

public class BackgroundPanel extends JPanel	{

	private static final String FONDO_NAME = "resources/mFondo";
	
	private ImageIcon imgFondo;
	
	public BackgroundPanel()	{

        try	{
        	imgFondo = new ImageIcon( getClass().getResource( FONDO_NAME ) );
        }	catch (NullPointerException e)	{
        	JOptionPane.showMessageDialog(null, "No se encontro la imagen de fondo estirable");
        	System.exit(0);
        }
	}
    /**
     * Draw a background image in panel
    **/
    public void paintComponent(Graphics g)    {
        g.drawImage( imgFondo.getImage(), 0, 0, imgFondo.getIconWidth(), imgFondo.getIconHeight(), null);
      //  g.drawImage( imgFondo.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        setOpaque(false);
        super.paintComponent(g);
    }

}

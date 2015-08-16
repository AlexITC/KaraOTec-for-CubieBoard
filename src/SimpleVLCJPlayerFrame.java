
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class SimpleVLCJPlayerFrame	extends JFrame	{
	private PlayerVLCJ playerPanel;
	public SimpleVLCJPlayerFrame()	{
		super();
		setUndecorated(true);
		playerPanel = new PlayerVLCJ();
		playerPanel.setActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)	{
				if	( e.getActionCommand().equals( PlayerVLCJ.CMD_PLAYING ) )	{
					pack();
				}
				else if	( e.getActionCommand().equals( PlayerVLCJ.CMD_STOPPED ) )	{
					pack();
				}
			}
		});
		this.add(playerPanel);
	}
	public void play(String path)	{
		playerPanel.play(path);
	}
}

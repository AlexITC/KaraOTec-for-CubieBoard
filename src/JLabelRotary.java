
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import java.util.*;

public class JLabelRotary extends JLabel implements ActionListener {
	/**
	 * 
	 */
	private boolean alive;
	private boolean isAlive()	{
		return	alive;
	}
	public void finalize()	{
		alive = false;
	}
	private String text;
	public JLabelRotary(final int VELOCITY, String STRING) {
		super(STRING, SwingConstants.CENTER);
		setText(STRING);
		text = STRING;
		alive = true;
		new SwingWorker<Void, String>() {

			@Override
			protected Void doInBackground() throws Exception {
				while	( isAlive() )	{
					text = text.substring( 1 ) + text.charAt(0);
					publish(text);
					Thread.sleep(VELOCITY);
				}
				return null;
			}

			@Override
			protected void process(List<String> list)	{
				setText( list.get( list.size() - 1 ) );
			}
		}.execute();
	//	Timer timer = new Timer (VELOCITY, this) ;
	//	timer.start();
	}
	
	public synchronized void setText(String text)	{
		super.setText(text);
		this.text = text;
	}
	
	public void actionPerformed(ActionEvent e){
		if	( getText().length() != 0 )
			setText(getText().substring(1,getText().length())+getText().charAt(0));
    }
}

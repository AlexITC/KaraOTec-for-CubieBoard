
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Splash	extends JFrame {
	private final String ICON_NAME = "sI.class";
	private final JLabel lblIcon, lblText;
	public Splash()	{
		super();
		this.setUndecorated(true);
		lblIcon = new JLabel();
		ImageIcon icon = new ImageIcon(	this.getClass().getResource(ICON_NAME) );
		lblIcon.setIcon( icon );
		String autor = "Derechos Reservados (c) Alexis Hernandez Valemzuela, Edgar Manuel Alarcon Gonzalez, Jose Ricardo Ramos Beltran, 2014 - EPICSOUNDCOMPANY@GMAIL.COM    ";
		lblText = new JLabelRotary(350, autor);
		lblText.setOpaque(true);
		lblText.setBackground(Color.BLACK);
		lblText.setForeground(Color.WHITE);
		add(lblIcon, BorderLayout.CENTER);
		add(lblText, BorderLayout.SOUTH);
		this.getContentPane().setBackground(Color.BLACK);
		this.setSize( icon.getIconWidth(), icon.getIconHeight() + 30);
	//	pack();
	}
}

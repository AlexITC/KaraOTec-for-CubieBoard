package com.cdg.instruction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Michel Buffa (buffa@unice.fr)
 * @version $Id
 */
public class CdgOptionsDialog extends JDialog
{
	JPanel panel1 = new JPanel();

	BorderLayout borderLayout1 = new BorderLayout();

	JTabbedPane jTabbedPane1 = new JTabbedPane();

	JPanel jPanel1 = new JPanel();

	JPanel jPanel2 = new JPanel();

	Border border1;

	TitledBorder titledBorder1;

	JSlider jSlider1 = new JSlider();

	GridLayout gridLayout1 = new GridLayout();

	JPanel jPanel3 = new JPanel();

	JButton jButton1 = new JButton();

	JLabel jLabel1 = new JLabel();

	JLabel jLabel2 = new JLabel();

	JLabel jLabel3 = new JLabel();

	JLabel jLabel4 = new JLabel();

	JPanel jPanel4 = new JPanel();

	Border border2;

	JLabel jLabel5 = new JLabel();

	JLabel jLabel6 = new JLabel();

	JLabel jLabel7 = new JLabel();

	JLabel jLabel8 = new JLabel();

	JLabel jLabel9 = new JLabel();

	GridBagLayout gridBagLayout2 = new GridBagLayout();

	JFrame parentFrame;

	JCheckBox jCheckBox1 = new JCheckBox();

	GridBagLayout gridBagLayout1 = new GridBagLayout();

	/**
	 * Constructor
	 * 
	 * @param frame
	 * @param title
	 * @param modal
	 */
	public CdgOptionsDialog(JFrame frame, String title, boolean modal)
	{
		super(frame, title, modal);
		
		
		parentFrame = frame;
		try
		{
			jbInit();
			jSlider1.setValue(20);
			pack();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Constructor
	 */
	public CdgOptionsDialog()
	{
		this(null, "", false);
	}

	private void jbInit() throws Exception
	{
		border1 = BorderFactory.createLineBorder(Color.black, 2);
		titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(Color.black, 1),
		         "Cdg buffer size");
		border2 = BorderFactory.createLineBorder(SystemColor.controlText, 1);
		panel1.setLayout(borderLayout1);
		this.setResizable(false);
		this.setTitle("CDG Options");
		jPanel1.setLayout(gridBagLayout1);
		jPanel2.setBorder(titledBorder1);
		jPanel2.setLayout(gridLayout1);
		jSlider1.setInverted(false);
		jSlider1.setMajorTickSpacing(5);
		jSlider1.setMaximum(40);
		jSlider1.setMinimum(0);
		jSlider1.setMinorTickSpacing(1);
		jSlider1.setPaintLabels(true);
		jSlider1.setPaintTicks(true);
		jButton1.setText("Ok");
		jButton1.addActionListener(new CdgOptionsDialog_jButton1_actionAdapter(this));
		jLabel1.setText("5  = Fast Computer/Graphic card");
		jLabel2.setText("10 = normal");
		jLabel3.setText("20 = slow");
		jLabel4.setText("30 = very slow");
		jPanel4.setBorder(border2);
		jPanel4.setDebugGraphicsOptions(0);
		jPanel4.setLayout(gridBagLayout2);
		jLabel5.setFont(new java.awt.Font("Dialog", 1, 11));
		jLabel5.setForeground(Color.red);
		jLabel5.setText("Changes will be taken");
		jLabel6.setFont(new java.awt.Font("Dialog", 1, 11));
		jLabel6.setForeground(Color.red);
		jLabel6.setText("into account when");
		jLabel7.setFont(new java.awt.Font("Dialog", 1, 11));
		jLabel7.setForeground(Color.red);
		jLabel7.setText("next song will be played");
		jLabel8.setFont(new java.awt.Font("Dialog", 1, 11));
		jLabel8.setText("If lyrics are not in sync with the music,");
		jLabel9.setFont(new java.awt.Font("Dialog", 1, 11));
		jLabel9.setText("try to increase the Cdg buffer size...");
		jCheckBox1.setText("Force redraw full Images (take more time, better antialiasing)");
		getContentPane().add(panel1);
		panel1.add(jTabbedPane1, BorderLayout.CENTER);
		this.getContentPane().add(jPanel3, BorderLayout.SOUTH);
		jPanel3.add(jButton1, null);
		jTabbedPane1.add(jPanel1, "Sync problems");
		jPanel1.add(jPanel2, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
		         GridBagConstraints.BOTH, new Insets(8, 8, 0, 10), 171, 14));
		jPanel2.add(jSlider1, null);
		jPanel4.add(jLabel6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(0, 27, 0, 32), 0, 0));
		jPanel4.add(jLabel5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(26, 14, 0, 19), 0, 0));
		jPanel4.add(jLabel7, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(0, 14, 30, 13), 0, 0));
		jPanel1.add(jCheckBox1, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0,
		         GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 16, 24, 32), 34,
		         -1));
		jPanel1.add(jLabel1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(6, 8, 12, 53), 8, 14));
		jPanel1.add(jLabel2, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(26, 8, 0, 53), 109, 6));
		jPanel1.add(jLabel3, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(0, 8, 25, 53), 117, 5));
		jPanel1.add(jLabel4, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(12, 8, 0, 53), 92, 12));
		jPanel1.add(jLabel9, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(0, 8, 0, 20), 0, 5));
		jPanel1.add(jLabel8, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
		         GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 6, 9));
		jPanel1.add(jPanel4, new GridBagConstraints(1, 1, 1, 4, 1.0, 1.0, GridBagConstraints.CENTER,
		         GridBagConstraints.BOTH, new Insets(12, 0, 0, 10), 0, 20));
	}

	// The Dialog will appear centered
	// The Dialog will appear centered
	// The window will appear centered
	public void setVisible(boolean flag)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2,
		         (screenSize.height - frameSize.height) / 2);
		super.setVisible(flag);
	}

	public boolean getRedrawFullImage()
	{
		return jCheckBox1.isSelected();
	}

	public void setRedrawFullImage(boolean flag)
	{
		jCheckBox1.setSelected(flag);
	}

	public int getCdgBufferSize()
	{
		int value = jSlider1.getValue();
		if (value == 0)
			return 1;
		return value;
	}

	public void setCdgBufferSize(int size)
	{
		jSlider1.setValue(size);
	}

	void jButton1_actionPerformed(ActionEvent e)
	{
		setVisible(false);
	}
}

class CdgOptionsDialog_jButton1_actionAdapter implements java.awt.event.ActionListener
{
	CdgOptionsDialog adaptee;

	CdgOptionsDialog_jButton1_actionAdapter(CdgOptionsDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.jButton1_actionPerformed(e);
	}
}

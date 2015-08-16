
package com.cdg.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import javax.swing.JPanel;

/**
 * In the CD+G system, 16 color graphics are displayed on a raster field which is 300 x 216 pixels
 * in size. The middle 294 x 204 area is within the TV's "safe area", and that is where the graphics
 * are displayed. The outer border is set to a solid color. The colors are stored in a 16 entry
 * color lookup table.
 * 
 * @author Michel Buffa (buffa@unice.fr)
 * @version $Id
 */
public class CdgGraphicBufferedImage extends JPanel
{

	// Colors that composes the indexed color model. 16 colors...
	private byte[] reds = new byte[16];

	private byte[] greens = new byte[16];

	private byte[] blues = new byte[16];

	private IndexColorModel icm;

	private BufferedImage img;

	private WritableRaster raster;

	private int width = 300, height = 216;

	private byte[] pixels = new byte[300 * 216];

	private byte[] savedPixels = new byte[300 * 216];

	// for damaged area to be redrawn
	private Rectangle fullRectangle = new Rectangle(300, 216);

	private Rectangle r = fullRectangle;

	private Rectangle rDestination = new Rectangle(300, 216);

	double sx, sy;

	private boolean forceRedrawFullImage = false;

	private boolean fromPartialRedraw = false;

	public CdgGraphicBufferedImage()
	{
		// by default, JPanel is double buffered
		setDoubleBuffered(false);
		// We do create a default colormodel with color 0 everywhere...
		setDefaultColormap();
		icm = new IndexColorModel(4, 16, reds, greens, blues);

		createImage();

		// Clear the initial screen
		Arrays.fill(pixels, (byte) 0);

		// setSize(300, 216);
		setPreferredSize(new Dimension(300, 216));

		// For tracking size
		addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				width = getWidth();
				height = getHeight();
				computeScaleFactors();
				// resizing = true;
				redrawFullImage();
			}
		});
		
	}

	private void setDefaultColormap()
	{
		for (int i = 0; i < reds.length; i++)
		{
			reds[i] = (byte) 0;
			greens[i] = (byte) 0;
			blues[i] = (byte) 0;
		}
	}

	public void setColormapHigh(Color[] colormap)
	{
		// We redraw the image only after a possible change of the high part
		// of the colromap as low and high colormap changes always come together
		if (setColormap(colormap, 8, 16))
		{
			createImage();
			redrawFullImage();
		}
	}

	public void setColormapLow(Color[] colormap)
	{
		setColormap(colormap, 0, 8);
	}

	private boolean setColormap(Color[] colormap, int startIndex, int stopIndex)
	{
		// Sometimes, many colormap changed occurs, consecutive identical
		// calls
		boolean changed = false;

		for (int i = startIndex; i < stopIndex; i++)
		{
			if (reds[i] != (byte) colormap[i].getRed())
			{
				reds[i] = (byte) colormap[i].getRed();
				changed = true;
			}
			if (greens[i] != (byte) colormap[i].getGreen())
			{
				greens[i] = (byte) colormap[i].getGreen();
				changed = true;
			}
			if (blues[i] != (byte) colormap[i].getBlue())
				blues[i] = (byte) colormap[i].getBlue();
			changed = true;
		}

		return changed;

	}

	private void createImage()
	{
		icm = new IndexColorModel(4, 16, reds, greens, blues);
		img = new BufferedImage(300, 216, BufferedImage.TYPE_BYTE_INDEXED, icm);
		raster = img.getRaster();
		raster.setDataElements(0, 0, 300, 216, pixels);
	}

	public void redrawFullImage()
	{
		// source rectangle : zone in the pixels byte array
		r = fullRectangle;

		computeDamagedDestinationRectangle(r);
		paintImmediately(rDestination);

	}

	private void redrawPartialImage(Rectangle r)
	{
		if (forceRedrawFullImage)
		{
			redrawFullImage();
		}
		else
		{
			// source rectangle : zone in the pixels byte array
			this.r = r;
			// r = new Rectangle(r.x, r.y, (int)Math.round(1.1*r.width),
			// (int)Math.round(1.1*r.height));

			// Destination rectangle on screen, maybe scaled
			computeDamagedDestinationRectangle(r);

			// paintImmediately(rDestination);
			fromPartialRedraw = true;
			paintImmediately(rDestination);
		}
	}

	private void computeDamagedDestinationRectangle(Rectangle r)
	{
		rDestination.x = (int) Math.round(r.x * sx);
		rDestination.y = (int) Math.round(r.y * sy);
		rDestination.width = (int) Math.round(r.width * sx) + 1;
		rDestination.height = (int) Math.round(r.height * sy) + 1;
	}

	public void pixelsChanged()
	{
		raster.setDataElements(0, 0, 300, 216, pixels);
		redrawFullImage();
	}

	public void pixelsChanged(Rectangle r)
	{
		// Maybe optimise the following instruction ?
		raster.setDataElements(0, 0, 300, 216, pixels);
		redrawPartialImage(r);
	}

	public void computeScaleFactors()
	{
		// called when the window or screen is resized

		sx = (width / 300.0);
		sy = (height / 216.0);

	}

	public void paintComponent(Graphics g)
	{
		if (fromPartialRedraw)
		{
			g.drawImage(img, rDestination.x, rDestination.y, rDestination.x + rDestination.width,
			         rDestination.y + rDestination.height, r.x, r.y, r.x + r.width, r.y + r.height,
			         this);
		}
		else
		{
			// System.out.println("Complete redraw");
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		}

		fromPartialRedraw = false;
	}

	public byte[] getPixels()
	{
		return pixels;
	}

	public void savePixels()
	{
		System.arraycopy(pixels, 0, savedPixels, 0, 300 * 216);
	}

	public void restorePixels()
	{
		System.arraycopy(savedPixels, 0, pixels, 0, 300 * 216);
	}

	public void setWindowedMode(boolean windowedMode)
	{
		// When in fullscreen modes, repaint() called by the AWT API have no
		// sense
		redrawFullImage();
		setIgnoreRepaint(!windowedMode);
	}

	public void setForceDrawFullImage(boolean forceRedrawFullImage)
	{
		this.forceRedrawFullImage = forceRedrawFullImage;
	}
}

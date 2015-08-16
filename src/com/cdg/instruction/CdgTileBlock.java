package com.cdg.instruction;

import java.awt.Rectangle;

/**
 * Description: These commands load a 12 x 6 tile of pixels from the subCode.data area. I recall
 * that in the original CD+G spec, the tile is refered to as a "font", but I think the word tile is
 * more appropriate, because the tile can (and does) contain any graphical image, not just text.
 * Larger images are built by using multiple tiles. The XOR variant is a special case of the same
 * command, the difference is described below. The tile is stored using 1-bit graphics. The
 * structure contains two colors which are to be used when rendering the tile. The tile is extracted
 * from 16 bytes of subCode.data[] in the following manner: byte color0; // Only lower 4 bits are
 * used, mask with 0x0F byte color1; // Only lower 4 bits are used, mask with 0x0F byte row; // Only
 * lower 5 bits are used, mask with 0x1F byte column; // Only lower 6 bits are used, mask with 0x3F
 * byte tilePixels[12]; // Only lower 6 bits of each byte are used color0, color1 describe the two
 * colors (from the color table) which are to be used when rendering the tile. Color0 is used for 0
 * bits, Color1 is used for 1 bits. Row and Column describe the position of the tile in tile
 * coordinate space. To convert to pixels, multiply row by 12, and column by 6. tilePixels[]
 * contains the actual bit values for the tile, six pixels per byte. The uppermost valid bit of each
 * byte (0x20) contains the left-most pixel of each scanline of the tile. In the normal instruction,
 * the corresponding colors from the color table are simply copied to the screen. In the XOR
 * variant, the color values are combined with the color values that are already onscreen using the
 * XOR operator. Since CD+G only allows a maximum of 16 colors, we are XORing the pixel values
 * (0-15) themselves, which correspond to indexes into a color lookup table. We are not XORing the
 * actual R,G,B values.
 * 
 * @author Michel Buffa (buffa@unice.fr)
 * @version $Id
 */
public class CdgTileBlock
{
	private static byte color0;

	private static byte color1;

	private static byte[] tilePixels = new byte[12];

	private static Rectangle rectangle = new Rectangle(0, 0, 6, 12);

	public static Rectangle drawTile(byte[] data, byte[] pixels, boolean xorDrawingMode)
	{
		// data is the 16 bytes array of the cdg chunk.
		// pixels is the colorbuffer. Each cell is an index

		// Only 4 lower bits are used. color0 and color1 are indexes in the
		// colormap
		color0 = (byte) (data[0] & 0x0F);
		color1 = (byte) (data[1] & 0x0F);

		// Only 5 lower bits are used
		byte row = (byte) (data[2] & 0x1F);
		// Only 6 lower bits are used
		byte column = (byte) (data[3] & 0x3F);

		// The tile itself, made of 1 (color1) and 0 (color0)
		for (int i = 0; i < 12; i++)
		{
			tilePixels[i] = (byte) (data[4 + i] & 0x3F);
		}

		// Now, we do compute the tile itself
		rectangle.x = column * 6;
		rectangle.y = row * 12;

		// address in the main graphic buffer, from where we start to fill
		// pixels
		int startAdress = rectangle.y * 300 + rectangle.x;

		// We paint line by line. A tile is 12 pixels vertical by 6 horizontal
		for (int i = 0; i < tilePixels.length; i++)
		{
			// For each line
			for (int j = 0; j < 6; j++)
			{
				// For each column, start processing the rightmost bit of the
				// tilePixels,
				// array, which in fact corresponds to the 6th pixel in the tile
				// (rightmost one). This explains the pixels[i * 6 + 5-j]
				// address :
				// i*6 is the start adress of the row, +6 in order to start with
				// the
				// bit on the right, -j in order to fill from 6th bit (index=5)
				// to first
				// one (index = 0)

				try
				{
					if ((tilePixels[i] & 0x1) == 0)
					{
						if (xorDrawingMode)
						{
							pixels[startAdress + (5 - j)] ^= color0;
						}
						else
						{
							pixels[startAdress + (5 - j)] = color0;
						}
					}
					else
					{
						if (xorDrawingMode)
						{
							pixels[startAdress + (5 - j)] ^= color1;
						}
						else
						{
							pixels[startAdress + (5 - j)] = color1;
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException ex)
				{
					System.out.println("Problem in reading tile block");
				}

				// Let's go for the next bit !
				tilePixels[i] = (byte) (tilePixels[i] >> 1);
			}
			startAdress += 300;
		}

		return rectangle;
	}
}

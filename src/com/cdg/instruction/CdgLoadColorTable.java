package com.cdg.instruction;

import java.awt.Color;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Load Color Table Lo [colors 0 thru 7] (subCode.instruction==30) Load Color Table
 * High [colors 8 thru 15] (subCode.instruction==31) These commands are used to load in the colors
 * for the color table. The colors are specified using 4 bits each for R, G and B, resulting in 4096
 * possible colors. The only difference between the two instructions is whether the low part or the
 * high part of the color table is loaded. In these instruction, the 16 byte data field is
 * interpreted as follows. short colorSpec[8]; // AND with 0x3F3F to clear P and Q channel in java a
 * short is 2 bytes, so the data field must be processed 2 bytes 2 bytes Each colorSpec value can be
 * converted to RGB using the following diagram: [---high byte---] [---low byte----] 7 6 5 4 3 2 1 0
 * 7 6 5 4 3 2 1 0 X X r r r r g g X X g g b b b b Note that P and Q channel bits need to be masked
 * off (they are marked here with Xs. The Load CLUT commands are often used in CD+G to provide the
 * illusion of animation through the use of color cyling.
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

public abstract class CdgLoadColorTable
{

	/**
	 * Parses the data chunk, and fills 8 colormap cells, starting at startIndex, which can be 0 or 8
	 * depending on the case we are processing a LoadColorTableLow or a LoadColorTableHigh cdg
	 * instruction
	 * 
	 * @param data :
	 *           the 16 bits data chunk that encode 8 colors
	 * @param startIndex :
	 *           0 or 8 (low/high colormap)
	 * @param colormap :
	 *           the colormap that is going to be half-filled
	 */
	public static void setColormap(byte[] data, int startIndex, Color[] colormap)
	{
		// System.out.println("CdgLoadColortable voici la colormap de d�part");
		// for (int i = 0; i < colormap.length; i++) {
		// System.out.println("cm[i] = " + colormap[i]);
		// }
		// System.out.println("-------");

		// data is a 16 bytes chunk, should be decoded 2 bytes by 2 bytes
		// Read info at the top of this file
		byte lowByte, highByte;
		byte red, blue, green;

		// We have to parse 8 colors
		for (int i = 0; i < 16; i += 2)
		{
			highByte = data[i];
			highByte &= 0x3f;

			lowByte = data[i + 1];
			lowByte &= 0x3f;

			// Keep only 4 lower bits of the lowByte
			blue = (byte) (lowByte & 0xF);
			// Keep only bits 3, 4, 5, 6, so shift 2 bits to the right in order
			// to throw the 2 first bits away !
			red = (byte) (highByte >> 2);
			green = (byte) (((highByte & 0x3) * 4) + (lowByte >> 4));

			// blue, green and red are color value in the range [0-15] as they
			// are
			// coded in 4 bits. Let's turn them into "real" modern colors in the
			// range [0-255]. Let's multiply the [0-15] value by 255, and divide
			// by 15
			// i.e multiply by 17 !
			setColormapElement(colormap, startIndex, i, (red * 17), (green * 17), (blue * 17));
		}
	}

	public static void setColormapElement(Color[] colormap, int startIndex, int index, int red,
	         int green, int blue)
	{
		// System.out.println("SetColormapElement LOW index=" + (index/2) +" red
		// = " + red + " green = " + green + " blue = " +blue);
		colormap[startIndex + index / 2] = new Color(red, green, blue);
	}

}

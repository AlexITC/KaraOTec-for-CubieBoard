
package com.cdg.instruction;


import java.util.Arrays;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: In this instruction, the 16 byte data field is interepreted as follows. byte color; //
 * Only lower 4 bits are used, mask with 0x0F byte filler[15]; Color refers to a color to clear the
 * screen to. The border area of the screen should be cleared to this color. The border area is the
 * area contained with a rectangle defined by (0,0,300,216) minus the interior pixels which are
 * contained within a rectangle defined by (6,12,294,204).
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

public class CdgBorderPreset
{
	static byte color;

	static final int WIDTH = 300;

	// byte []filler = new byte[15];

	public static void drawBorder(byte[] data, byte[] pixels)
	{
		// data is the 16 bytes array of the cdg chunk.
		// For this one, only the lower 4 bits are used
		color = (byte) (data[0] & 0x0F);

		Arrays.fill(pixels, 0, 12 * WIDTH, color); // top
		Arrays.fill(pixels, 204 * WIDTH, (204 * WIDTH) + (12 * WIDTH), color); // bottom

		for (int i = 12; i < 204; i++)
		{ // sides
			Arrays.fill(pixels, i * WIDTH, (i * WIDTH) + 6, color); // left
			Arrays.fill(pixels, i * WIDTH + 294, ((i * WIDTH) + 294) + 6, color); // right
		}
	}
}

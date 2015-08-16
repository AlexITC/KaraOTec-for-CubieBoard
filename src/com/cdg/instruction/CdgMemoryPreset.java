package com.cdg.instruction;

import java.util.Arrays;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Memory Filler instruction In this instruction, the 16 byte data field is
 * interepreted as follows. byte color; // Only lower 4 bits are used, mask with 0x0F byte repeat; //
 * Only lower 4 bits are used, mask with 0x0F byte filler[14]; When these commands appear in bunches
 * (to insure that the screen gets cleared), the repeat count is used to number them. If this is
 * true, and you have a reliable data stream, you can ignore the command if repeat != 0. Note that
 * in all these instructions, only the lower 6 bits of each data byte are used. So, for example, If
 * I refer to subCode.data[0], I actually mean (subCode.data[0] & 0x3F).
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version $Id
 */

public class CdgMemoryPreset
{
	// Color refers to a color to clear the screen to. The entire screen should
	// be cleared to this color.
	// Only lower 4 bits are used, mask with 0x0F
	private static byte color;

	private static byte repeat;

	private static byte[] filler = new byte[14];

	public static boolean clearScreen(byte[] data, byte[] pixels)
	{
		// data is the 16 bytes array of the cdg chunk.

		// For these, only the lower 4 bits are used
		color = (byte) (data[0] & 0x0F);
		repeat = (byte) (data[1] & 0x0F);

		if (repeat != 0)
			return false;

		// Only the lower 6 bits of
		// each data byte of the filler are used. Everything should be anded
		// with 0x3F.
		for (int i = 0; i < 14; i++)
		{
			filler[i] = (byte) (data[2 + i] & 0x3F);
		}

		// System.out.println("Clear the screen with color = " + color);
		/*
		 * for (int i = 0; i < 216 * 300; i++) { pixels[i] = color; }
		 */
		clearScreen(pixels, color);
		return true;
	}

	public static void clearScreen(byte[] pixels, byte color)
	{
		// System.out.println("Clear the screen with color = " + color);
		/*
		 * for (int i = 0; i < 216 * 300; i++) { pixels[i] = color; }
		 */

		Arrays.fill(pixels, color);
	}

}

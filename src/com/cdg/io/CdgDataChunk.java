
package com.cdg.io;

import java.awt.Color;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: this class describe a typical cdg data chunk of 1+1+2+16+4 = 24 bytes, as stated in
 * the philipps red book. A CDG file is a list of chunks of that type.
 * </p>
 * The SubCode.command and SubCode.instruction fields in each SubCode packet are used to determine
 * how the data is to be interpreted. I will enumerate these commands below. When reading the
 * command and instruction fields, you should only pay attention to the lower 6 bits of each of
 * these bytes (e.g. by ANDing the byte with the hex value 3Fh). The upper 2 bits are used for the P
 * and Q channels which you should ignore. If the lower 6 bits of the command field are equal to 9,
 * then the SubCode packet contains CD+G information, otherwise it should be ignored. If the packet
 * contains CD+G information, then you use the instruction field (again, the lower six bits), to
 * determine how to interpret the data. An example will be shown below.
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

public class CdgDataChunk
{
	// Here are the 24 bytes of the cdg chunk
	private byte cdgCommand;

	private byte cdgInstruction;

	private byte[] parityP = new byte[2];

	private byte[] cdgData = new byte[16];

	private byte[] parityQ = new byte[4];

	// its offset in the file
	private int offset;

	// The current colormap
	private static Color[] colormap = new Color[16];

	// All the constants used for the cdgInstruction
	// Set the screen to a particular color.
	public static byte CDG_MEMORY_PRESET = 1;

	// Set the border of the screen to a particular color.
	public static byte CDG_BORDER_PRESET = 2;

	// Load a 12 x 6, 2 color tile and display it normally.
	public static byte CDG_TILE_NORMAL = 6;

	// Scroll the image, filling in the new area with a color.
	public static byte CDG_SCROLL_PRESET = 20;

	// Scroll the image, rotating the bits back around.
	public static byte CDG_SCROLL_COPY = 24;

	// Define a specific color as being transparent.
	public static byte CDG_DEF_TRANS_COLOR = 28;

	// Load in the lower 8 entries of the color table.
	public static byte CDG_LOAD_COL_TABLE_LOW = 30;

	// Load in the upper 8 entries of the color table.
	public static byte CDG_LOAD_COL_TABLE_HIGH = 31;

	// Load a 12 x 6, 2 color tile and display it using the XOR method.
	public static byte CDG_TILE_XOR = 38;

	public CdgDataChunk(byte[] data, int offset)
	{
		this.offset = offset;

		// Initialize attributes from the 24 bytes data array
		// For the command and instruction, only the lower 6 bits are relevant,
		// that's why we and them with ox3f
		cdgCommand = (byte) (data[offset + 0] & 0x3f);
		cdgInstruction = (byte) (data[offset + 1] & 0x3f);
		parityP[0] = data[offset + 2];
		parityP[1] = data[offset + 3];

		// The 16 bytes data chunk
		for (int i = 0; i < 16; i++)
		{
			cdgData[i] = data[offset + 4 + i];
		}

		parityQ[0] = data[offset + 20];
		parityQ[0] = data[offset + 21];
		parityQ[0] = data[offset + 22];
		parityQ[0] = data[offset + 23];
	}

	public byte getCdgCommand()
	{
		return cdgCommand;
	}

	public byte[] getCdgData()
	{
		return cdgData;
	}

	public byte getCdgInstruction()
	{
		return cdgInstruction;
	}

	public byte[] getParityP()
	{
		return parityP;
	}

	public byte[] getParityQ()
	{
		return parityQ;
	}

	public String timeOffset()
	{
		// As guessed from the EditCDG software, it seems that a decimal
		// offset of 7200 corresponds to 1000 ms, so here we go !
		long ms = (offset * 10) / 72;

		long seconds = ms / 1000;
		long remainingMs = ms % 1000;

		long minutes = seconds / 60;
		long remainingSeconds = seconds % 60;

		return "" + minutes + ":" + remainingSeconds + ":" + remainingMs;
	}

	public String toString()
	{
		String description = "";

		if (cdgCommand == 9)
		{
			// Then we've got a cdg instruction there... note : cdgCommand and
			// cdgInstruction have been already anded with 0x3f in order to keep
			// only the lower six bits, which are relevants
			// description += "dec file offset : " + offset + " time : " +
			// timeOffset() + "\t";

			if (cdgInstruction == CDG_MEMORY_PRESET)
				description += "Memory Preset";
			else if (cdgInstruction == CDG_BORDER_PRESET)
				description += "Border Preset";
			else if (cdgInstruction == CDG_TILE_NORMAL)
				description += "Tile Block(normal)";
			else if (cdgInstruction == CDG_SCROLL_PRESET)
				description += "Scroll Preset";
			else if (cdgInstruction == CDG_SCROLL_COPY)
				description += "Scroll Copy";
			else if (cdgInstruction == CDG_DEF_TRANS_COLOR)
				description += "Def trans color";
			else if (cdgInstruction == CDG_LOAD_COL_TABLE_LOW)
			{
				description += "Load color table (entries 0-7)";
			}
			else if (cdgInstruction == CDG_LOAD_COL_TABLE_HIGH)
			{
				description += "Load color table (entries 8-15)";
			}
			else if (cdgInstruction == CDG_TILE_XOR)
				description += "Tile Block(xor)";
		}

		return description;
	}

	public void display()
	{
		String description = toString();

		if (!description.equals(""))
			System.out.println(description);
	}

	public Color[] getColormap()
	{
		return colormap;
	}
}

package com.cdg.instruction;

/**
 * Scroll Preset (subCode.instruction==20) and Scroll Copy (subCode.instruction==24) In these
 * instruction, the 16 byte data field is interepreted as follows. byte color; // Only lower 4 bits
 * are used, mask with 0x0F byte hScroll; // Only lower 6 bits are used, mask with 0x3F byte
 * vScroll; // Only lower 6 bits are used, mask with 0x3F This command is used to scroll all the
 * pixels on the screen horizontally and/or vertically. The color refers to a fill color to use for
 * the new area uncovered by the scrolling action. It is only used in the Scroll Preset command. In
 * the Scroll Copy command the screen is "rotated" around. For example, in scrolling to the left,
 * pixels uncovered on the right are filled in by the pixels being scrolled off the screen on the
 * left. The hScroll field is a compound field. It can be divided into two fields like so: SCmd =
 * (hScroll & 0x30) >> 4; HOffset = (hScroll & 0x07); SCmd is a scrolliing instruction, which is
 * either 0, 1 or 2. 0 means don't scroll 1 means scroll 6 pixels to the right, 2 means scroll 6
 * pixels to the left. HOffset is a horizontal offset which is used for offsetting the graphic
 * display by amounts less than 6 pixels. It can assume values from 0 to 5. Similarly, the vScroll
 * field is a compound field. It can be divided into two fields like so: SCmd = (vScroll & 0x30) >>
 * 4; VOffset = (vScroll & 0x0F); SCmd is a scrolliing instruction, which is either 0, 1 or 2. 0
 * means don't scroll 1 means scroll 12 pixels down, 2 means scroll 12 pixels up. VOffset is a
 * vertical offset which is used for offsetting the graphic display by amounts less than 12 pixels.
 * It can assume values from 0 to 11. Smooth horizontal and vertical scrolling in all directions can
 * be done by combining scroll commands. For example, here is a smooth horizontal scroll to the
 * left: SCmd HScroll === ======= 0 1 0 2 0 3 0 4 0 5 0 6 2 0 (repeat) You can create the effect of
 * an infinite panorama by continually loading in new tiles into the border area and scrolling them
 * into view.
 * 
 * @author Michel Buffa (buffa@unice.fr)
 */
public class CdgScroll
{
	// private byte color;

	// private byte hScroll;

	// private byte vScroll;

	public CdgScroll(byte[] data)
	{
	// data is the 16 bytes array of the cdg chunk.

	// Only 4 lower bits are used
	// color = (byte) (data[0] & 0x0F);

	// Only lower 6 bits are used
	// hScroll = (byte) (data[1] & 0x3F);
	// vScroll = (byte) (data[2] & 0x3F);
	}
}

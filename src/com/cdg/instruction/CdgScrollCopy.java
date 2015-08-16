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
 * It can assume values from 0 to 11. You can create the effect of an infinite panorama by
 * continually loading in new tiles into the border area and scrolling them into view.
 */
public class CdgScrollCopy
{
	public static final int WIDTH = 300;

	public static final int HEIGHT = 216;

	/**
	 * @return true if a scroll is done, false if only an offset is done
	 */
	public static boolean scroll(byte data[], byte pixels[])
	{
		byte[] trash = new byte[12 * WIDTH]; // max size
		// byte color;
		byte hcmd;
		byte vcmd;
		byte hOffset;
		byte vOffset;
		byte h = 0, v = 0;
		boolean ret = false;

		// Only 4 lower bits are used
		// color = (byte) ((data[0] & 0x0F) + 2);

		// Only lower 6 bits are used
		hcmd = (byte) ((data[1] & 0x30) >> 4);
		vcmd = (byte) ((data[2] & 0x30) >> 4);
		hOffset = (byte) ((data[1] & 0x7) % 6);
		vOffset = (byte) ((data[2] & 0xF) % 12);

		switch (hcmd)
		{
			case 0:
				h = hOffset;
				break;
			case 2: // left
				h = 6;
				ret = true;
				break;
			case 1: // right
				h = -6;
				ret = true;
				break;
			default:
				break;
		}

		if (h > 0)
		{
			for (int i = 0; i < HEIGHT; i++)
			{
				// memcpy(trash,&pixels[(i*WIDTH)],h);
				System.arraycopy(pixels, (i * WIDTH), trash, 0, h);
				// memmove(&pixels[(i*WIDTH)],&pixels[(i*WIDTH)+h],WIDTH-h);
				System.arraycopy(pixels, (i * WIDTH) + h, pixels, (i * WIDTH), WIDTH - h);
				// memcpy(&pixels[(i*WIDTH)+WIDTH-h],trash,h);
				System.arraycopy(trash, 0, pixels, (i * WIDTH) + WIDTH - h, h);
			}
		}
		else
		{
			h *= -1;
			for (int i = 0; i < HEIGHT; i++)
			{
				// memcpy(trash,&pixels[(i*WIDTH)+WIDTH-h],h);
				System.arraycopy(pixels, (i * WIDTH) + WIDTH - h, trash, 0, h);
				// memmove(&pixels[(i*WIDTH)+h],&pixels[(i*WIDTH)],WIDTH-h);
				System.arraycopy(pixels, (i * WIDTH), pixels, (i * WIDTH) + h, WIDTH - h);
				// memcpy(&pixels[(i*WIDTH)],trash,h);
				System.arraycopy(trash, 0, pixels, (i * WIDTH), h);

			}
		}

		switch (vcmd)
		{
			case 0:
				v = vOffset;
				break;
			case 2: // up
				v = 12;
				ret = true;
				break;
			case 1: // down
				v = -12;
				ret = true;
				break;
			default:
				break;
		}

		if (v > 0)
		{
			// memcpy(trash,pixels,WIDTH*(v));
			System.arraycopy(pixels, 0, trash, 0, WIDTH * (v));
			// memmove(pixels,&pixels[(v)*WIDTH],WIDTH*(HEIGHT-v));
			System.arraycopy(pixels, (v) * WIDTH, pixels, 0, WIDTH * (HEIGHT - v));
			// memcpy(&pixels[(HEIGHT-v)*WIDTH],trash,WIDTH*(v));
			System.arraycopy(trash, 0, pixels, (HEIGHT - v) * WIDTH, WIDTH * (v));
		}
		else
		{
			v *= -1;
			// memcpy(trash,&pixels[(HEIGHT-v)*WIDTH],WIDTH*(v));
			System.arraycopy(pixels, (HEIGHT - v) * WIDTH, trash, 0, WIDTH * (v));
			// memmove(&pixels[(v)*WIDTH],pixels,WIDTH*(HEIGHT-v));
			System.arraycopy(pixels, 0, pixels, (v) * WIDTH, WIDTH * (HEIGHT - v));
			// memcpy(pixels,trash,WIDTH*(v));
			System.arraycopy(trash, 0, pixels, 0, WIDTH * (v));
		}
		return ret;
	}
}

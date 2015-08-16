/**
 * BasicPlayer.
 *
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

package com.player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

/**
 * This has additional functionality not built into the jlGUI BasicPlayer
 */
public abstract class BasicPlayer implements BasicController
{
	/**
	 * These variables are used to distinguish stopped, paused, playing states. We need them to
	 * control Thread.
	 */
	public static final int UNKNOWN = -1;

	public static final int PLAYING = 0;

	public static final int PAUSED = 1;

	public static final int STOPPED = 2;

	public static final int OPENED = 3;

	public static final int SEEKING = 4;

	protected int m_status = UNKNOWN;

	// Listeners to be notified.
	protected Collection m_listeners = new ArrayList();

	protected String[] supportedFileTypeExtensions = {};


	public void addBasicPlayerListener(BasicPlayerListener bpl)
	{
		m_listeners.add(bpl);
	}

	/**
	 * Returns BasicPlayer status.
	 * 
	 * @return status
	 */
	public int getStatus()
	{
		return m_status;
	}

	/**
	 * Notify listeners about a BasicPlayerEvent.
	 * 
	 * @param code
	 *           event code.
	 * @param position
	 *           in the stream when the event occurs.
	 */
	protected void notifyEvent(int code, int position, double value, Object description)
	{
		Iterator it = m_listeners.iterator();
		while (it.hasNext())
		{
			BasicPlayerListener bpl = (BasicPlayerListener) it.next();
			bpl.stateUpdated(new BasicPlayerEvent(this, code, position, value, description));
		}
	}

	/**
	 * Checks if the file is in a supported format
	 * 
	 * @param file
	 * @return true if file is supported, false otherwise
	 */
	public boolean isFileSupported(File file)
	{
		return isFileSupported(file.getName());
	}

	/**
	 * Checks if the file is in a supported format
	 * 
	 * @param filename
	 * @return true if file is supported, false otherwise
	 */
	public boolean isFileSupported(String filename)
	{
		System.out.println("On teste pour " + filename);
		for (int i = 0; i < supportedFileTypeExtensions.length; i++)
		{
			System.out.println("On compare avec " + supportedFileTypeExtensions[i]);
			if (filename.toLowerCase().endsWith(supportedFileTypeExtensions[i]))
				return true;
		}
		return false;
	}

	public String[] getSupportedFileTypeExtensions()
	{
		return supportedFileTypeExtensions;
	}

	public abstract void setSupportedFileTypeExtensions();

}

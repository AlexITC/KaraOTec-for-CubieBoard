
package com.cdg.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: this class describes a cdg file as an object
 * </p>
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

public class CdgFileObject
{
	private CdgDataChunk[] cdgDataChunksArray;

	public CdgFileObject(String filename) throws IOException
	{
		readFileAsAByteArray(filename);
	}

	private void readFileAsAByteArray(String fileName) throws IOException
	{
		File file = new File(fileName);
		FileInputStream in = new FileInputStream(file);

		// Allocate the array that will contain all the CdgDataChunks
		int fileLength = (int) file.length();
		int nbOfCdgDataChunks = fileLength / 24;
		// CdgDataChunk []cdgDataChunksArrayTmp = new
		// CdgDataChunk[nbOfCdgDataChunks];
		cdgDataChunksArray = new CdgDataChunk[nbOfCdgDataChunks];
		System.out.println("CdgDataChunks Array allocated, File length = " + fileLength
		         + " nb of data Chunks = " + nbOfCdgDataChunks);

		// The file is made of CdgDataChunks, each composed of 24 bytes
		byte[] data = new byte[fileLength];
		int bytes_read = in.read(data);
		System.out.println("Nb of bytes read = " + bytes_read);

		// Then, from the data byte array, build an array made of CdgDataChunks
		int nb = 0;
		for (int i = 0; i < fileLength; i += 24)
		{
			// Create a CdgDataChunk object then
			CdgDataChunk dataChunk = new CdgDataChunk(data, i);

			// Adds the data chunk to the array only if cdgCommand is equal to 9
			// Otherwise it is not an intresting cdg chunk...
			/*
			 * if(dataChunk.getCdgCommand() == 9) cdgDataChunksArrayTmp[nb++] = dataChunk;
			 */
			cdgDataChunksArray[nb++] = dataChunk;
		}

		// Copy the array into a smaler array, as only elements with command = 9
		// are of interest
		/*
		 * cdgDataChunksArray = new CdgDataChunk[nb]; for (int i = 0; i < cdgDataChunksArray.length;
		 * i++) { cdgDataChunksArray[i] = cdgDataChunksArrayTmp[i]; }
		 */
		in.close();
	}

	public void display()
	{
		for (int i = 0; i < cdgDataChunksArray.length; i++)
		{
			cdgDataChunksArray[i].display();
		}
	}

	public static void main(String[] args) throws Exception
	{
		CdgFileObject cdg = new CdgFileObject("test.cdg");
		cdg.display();
	}

	public CdgDataChunk[] getCdgDataChunksArray()
	{
		return cdgDataChunksArray;
	}
}

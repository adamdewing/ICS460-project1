package com.metrostate.ics460.project1.receiver;

import java.util.List;

/**
 * Course: 				ICS 460 Networks And Security
 * Project Description: A project uses UDP protocol to send and a receive a binary file.
 * 						The sender accepts a file as a command line parameter (any binary 
 * 						file on your hard disk), breaks it into smaller chunks.
 * Class Description: 	An interface to read and save a file onto disk
 * Instructor: 			Professor Demodar Chetty
 * @author Natnael Alemayehu
 *
 */

/**
 * An interface to read and save a file onto disk *
 */
public interface Saver {
	
	/**
	 * A public method save a file to a disk
	 * @param a list of byte array
	 */
	public void saveFile(byte[] bytes);

}




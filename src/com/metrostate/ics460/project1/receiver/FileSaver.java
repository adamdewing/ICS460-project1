package com.metrostate.ics460.project1.receiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.metrostate.ics460.project1.FileProcessor;

/**
 * Course: 		ICS 460 Networks And Security
 * Project Description: A project uses UDP protocol to send and a receive a binary file.
 * 			The sender accepts a file as a command line parameter (any binary 
 * 			file on your hard disk), breaks it into smaller chunks.
 * Class Description: 	A class FileSaver implements a FileProcessor interface to read a file from a disk 
 * 			and save a file onto disk
 * Instructor: 		Professor Demodar Chetty
 * @author Natnael Alemayehu
 *
 */
public class FileSaver implements FileProcessor {

	private List<byte[]> lines = new ArrayList<byte[]>();

	/**
	* A method save a file onto a disk it takes a list of byte aarys as an argument
	*/
	public void saveFile(byte[] byteList) {
		File file = new File("output");
		FileOutputStream fis = null;
		try {
			fis = new FileOutputStream(file);
			fis.write(byteList);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<byte[]> fileLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fileSaver(List<byte[]> byteList) {
		// TODO Auto-generated method stub
		
	}

}

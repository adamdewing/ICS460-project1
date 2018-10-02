package com.metrostate.ics460.project1.receiver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	public void saveFile(List<byte[]> byteList) {
		File file = new File("output.txt");
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter);
			for(byte[] bytes : byteList) {
				printWriter.println(new String(bytes, "UTF-8"));
			}
			
			printWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<byte[]> loadFile() {
		// TODO Auto-generated method stub
		return null;
	}
}

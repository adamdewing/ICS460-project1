package com.metrostate.ics460.project1.sender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

public class FileLoader implements FileProcessor {

	private String fileName;
	private String line;
	private List<byte[]> lines = new ArrayList<byte[]>();

	public List<byte[]> loadFile() {
		JFileChooser file_Chooser = new JFileChooser();
		file_Chooser.setCurrentDirectory(new File("."));
		if (file_Chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fileName = file_Chooser.getSelectedFile().getName();

			try {
				FileReader fileReader = new FileReader(new File(fileName));
				BufferedReader bufferedReader = new BufferedReader(fileReader);


				while ((line = bufferedReader.readLine()) != null) {
					//convert the String input into the byte array.
					lines.add(line.getBytes("US-ASCII"));
					//setLines(line.getBytes("US-ASCII"));	
				}

				fileReader.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lines;

	}

package com.metrostate.ics460.project1.sender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

public class FileLoader {
	private JFileChooser file_Chooser;
	private BufferedReader bufferedReader;
	private FileReader fileReader;

	public void loadFile() {
	String fileName;
		file_Chooser = new JFileChooser();
		file_Chooser.setCurrentDirectory(new File("."));
		if (file_Chooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION) {
			
			try {
				 fileName = file_Chooser.getSelectedFile().getName();
				fileReader = new FileReader(fileName);
				bufferedReader = new BufferedReader(fileReader);
				String line;
				
				while ((line = bufferedReader.readLine()) != null) {
				System.out.println(fileName);
					System.out.println(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("oops ... File not exist");
		}
	}

}
